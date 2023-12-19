package com.holoyolo.app.accBookHistory.web;

import java.time.Duration;import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.holoyolo.app.accBookHistory.service.AccBookHistoryService;
import com.holoyolo.app.accBookHistory.service.AccBookHistoryVO;
import com.holoyolo.app.accBookHistory.service.apiVO.CardRequestBodyVO;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoService;

import com.google.gson.Gson;

@Service
public class ApiCall {
    private static final String REQUEST_URL = "https://developers.nonghyup.com/InquireCreditCardAuthorizationHistory.nh";

    private final RestTemplate restTemplate;
    
	@Autowired 
	AccBookHistoryService accBookHistoryService;
	
	@Autowired
	MemberFinanceInfoService memberFinanceInfoService;

    public ApiCall(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void getPosts(String id) {
    	//http요청 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        
        //http 헤더의 Content-Type을 application/json으로 설정
        headers.setContentType(MediaType.APPLICATION_JSON);

        //요청 Body 인스턴스 생성
        CardRequestBodyVO request = new CardRequestBodyVO();
        
        //인스턴스를 Gson을 이용해 json형태로 변환
        Gson gson = new Gson();
        String jsonReq = gson.toJson(request);

        //요청 HttpEntity 생성, 헤더와 본문(jsonReq)을 포함
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonReq, headers);

        //POST요청보내고, 응답을 ResponseEntity 타입으로 받음
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(REQUEST_URL, requestEntity, String.class);

        //응답의 Body만 가져옴
        String responseBody = responseEntity.getBody();
        JSONParser parser = new JSONParser();
        try {
        	//응답받은 Body를 JSONObject로 파싱
			JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
			
			//REC라는 key의 값이 카드거래내역 값이기 때문에 JSONArray로 가져옴
			JSONArray recArray = (JSONArray) jsonObject.get("REC");
			
			//회원의 가장 최근 거래내역 날짜 가져와서, 그 날부터 현재까지의 날짜의 수 계산
			String latest = accBookHistoryService.getLatestPayDate(id);
			long dayDuration = duration(latest);
			
			if(dayDuration != 0) {			
				//거래내역이 없는 날부터 현재까지의 거래내역을 처리
				for(int z = 0; z < dayDuration; z++) {
					pushData(recArray , z, id);
				}
			}
			
		} catch (ParseException e) {
			// JSON 파싱 중 오류 발생하면 오류 메시지 출력
			e.printStackTrace();
		} 
    }
    
    
    public long duration(String latest) {
    	 // 문자열을 LocalDateTime으로 변환
        LocalDateTime latestDate = LocalDateTime.parse(latest, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("최근날짜 또는 카드등록날짜 : "+latestDate);
        // 현재 날짜 및 시간을 가져오기
        LocalDateTime currentDate = LocalDateTime.now();

        // 두 날짜 사이의 차이 계산
        Duration duration = Duration.between(latestDate, currentDate);

        // 차이를 일로 변환
        long days = duration.toDays();
    	return days;
    }
    
    public void pushData(JSONArray recArray, int z, String id) {
    	int j = 0;
		for (int i = recArray.size() - 1; i >= 0; i--) {
		    Object recObject = recArray.get(i);
            if (recObject instanceof JSONObject) {
                // 각 요소가 JSONObject인 경우 처리
                JSONObject recItem = (JSONObject) recObject;
                
                AccBookHistoryVO acc = new AccBookHistoryVO();
                if(((String)recItem.get("Ccyn")).equals("1")) {
                	acc.setInputOutput("GB1");
                }
                else {
                	acc.setInputOutput("GB2");
                }
                acc.setPaymentType("GA3");
                //회원카드회사 가져오기
                Map <String, String> cardMap = new HashMap<>();
                cardMap = memberFinanceInfoService.getCardInfo(id);
                acc.setBankname(cardMap.get("카드회사"));
                
                int minAmount = 100;
                int maxAmount = 20000;

                // 랜덤 객체 생성
                Random random = new Random();

                // 랜덤한 금액 생성 (100원 단위로)
                int randomAmount = minAmount + 100 * random.nextInt((maxAmount - minAmount) / 100 + 1);
                acc.setPrice(randomAmount);
                acc.setPayStore((String) recItem.get("AfstNm"));
                //회원아이디 세션에서 가져오기
                acc.setMemberId(id);
                
                LocalDate currentDate = LocalDate.now();
                LocalDate adjustedDate = currentDate.minusDays(z);
                System.out.println(adjustedDate);
                acc.setPayDate(adjustedDate);

                System.out.println("<<<<"+i+">>>>>");
                System.out.println(recItem);
//                i++;
                
                accBookHistoryService.insertAcc(acc);
                j++;
                if(j == 5) {
                	break;
                }
                
            }
        }
    }

    
}
