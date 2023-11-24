package com.holoyolo.app.accBookHistory.web;

import java.time.Duration;
import java.time.LocalDate;
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

import lombok.Data;
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        CardRequestBodyVO request = new CardRequestBodyVO();
        
        Gson gson = new Gson();
        String jsonReq = gson.toJson(request);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonReq, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(REQUEST_URL, requestEntity, String.class);

        // Handle the response if needed
        String responseBody = responseEntity.getBody();
        JSONParser parser = new JSONParser();
        try {
			JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
			JSONArray recArray = (JSONArray) jsonObject.get("REC");
			
			String latest = accBookHistoryService.getLatestPayDate(id);
			long dayDuration = duration(latest);

			System.out.println("날짜 차이 : "+dayDuration);
			
			
			if(dayDuration == 0) {
				System.out.println("ss");
			}
			else {				
				for(int z = 0; z < dayDuration; z++) {
					pushData(recArray , z, id);
				}
				
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
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
                	acc.setInputOutput("GA1");
                }
                else {
                	acc.setInputOutput("GA2");
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
