package com.holoyolo.app.accBookHistory.web;

import java.time.Duration;import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

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

import lombok.Data;
import com.google.gson.Gson;

@Service
public class ApiCall {
    private static final String REQUEST_URL = "https://developers.nonghyup.com/InquireCreditCardAuthorizationHistory.nh";

    private final RestTemplate restTemplate;
    
	@Autowired 
	AccBookHistoryService accBookHistoryService;

    public ApiCall(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void getPosts() {
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
			
			String latest = accBookHistoryService.getLatestPayDate();
			long dayDuration = duration(latest);

			System.out.println("날짜 차이 : "+dayDuration);
			
			
			if(dayDuration == 0) {
				System.out.println("ss");
			}
			else {				
				for(int z = 1; z < dayDuration; z++) {
					pushData(recArray , z);
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

        // 현재 날짜 및 시간을 가져오기
        LocalDateTime currentDate = LocalDateTime.now();

        // 두 날짜 사이의 차이 계산
        Duration duration = Duration.between(latestDate, currentDate);

        // 차이를 일로 변환
        long days = duration.toDays();
    	return days;
    }
    
    public void pushData(JSONArray recArray, int z) {
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
                acc.setBankname("농협");
                acc.setPrice(Integer.parseInt((String) recItem.get("Usam")));
                acc.setPayStore((String) recItem.get("AfstNm"));
                //회원아이디 세션에서 가져오기
                acc.setMemberId("testminju@mail.com");
                
                LocalDate currentDate = LocalDate.now();
                LocalDate adjustedDate = currentDate.minusDays(z);
                System.out.println(adjustedDate);
                acc.setPayDate(adjustedDate);

                System.out.println("<<<<"+i+">>>>>");
                System.out.println(recItem);
//                i++;
                
                accBookHistoryService.insertAccApi(acc);
                j++;
                if(j == 5) {
                	break;
                }
                
            }
        }
    }

    
}
