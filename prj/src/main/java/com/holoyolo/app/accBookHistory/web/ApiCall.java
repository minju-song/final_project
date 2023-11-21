package com.holoyolo.app.accBookHistory.web;

import java.time.LocalDate;
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
			
			int i = 1;
            for (Object recObject : recArray) {
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
                    acc.setPayDate(LocalDateTime.now());
                    System.out.println("<<<<"+i+">>>>>");
                    System.out.println(recItem);
                    i++;
                    accBookHistoryService.insertAccApi(acc);
                }
            }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        
    }


    
}
