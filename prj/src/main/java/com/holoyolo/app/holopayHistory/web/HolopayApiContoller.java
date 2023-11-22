package com.holoyolo.app.holopayHistory.web;

import java.util.Date;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.holoyolo.app.holopayHistory.service.HoloPayHistoryService;
import com.holoyolo.app.holopayHistory.service.HoloPayHistoryVO;
import com.holoyolo.app.holopayHistory.service.HolopayReqVO;

@Service
public class HolopayApiContoller {

	private static final String REQUEST_URL = "https://developers.nonghyup.com/DrawingTransfer.nh";

	private RestTemplate restTemplate;

	@Autowired
	HoloPayHistoryService holoPayHistoryService;

	public HolopayApiContoller(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void getPosts(int amount) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HolopayReqVO request = new HolopayReqVO(amount);

		Gson gson = new Gson();
		String jsonReq = gson.toJson(request);

		HttpEntity<String> requestEntity = new HttpEntity<>(jsonReq, headers);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(REQUEST_URL, requestEntity, String.class);

		// 응답 처리 필요시
		String responseBody = responseEntity.getBody();
		JSONParser parser = new JSONParser();
		try {
			// JSON 응답에서 필요한 정보 추출
			JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
			String finAcno = jsonObject.getString("FinAcno");
			JSONObject headerObj = jsonObject.getJSONObject("Header");
			String trtm = headerObj.getString("Trtm");
			String rsms = headerObj.getString("Rsms");

			// 다른 필드 필요시 추출...

			System.out.println("FinAcno: " + finAcno);
			System.out.println("Trtm: " + trtm);
			System.out.println("Rsms: " + rsms);
			   // 1. Date 객체 생성 (현재날짜)
			Date date = new Date();         
						
			if (rsms.contains("정상")) {
				HoloPayHistoryVO hph = new HoloPayHistoryVO();
				hph.setHpHistoryId(0);
				hph.setMemberId("JINU@mail.com");
				hph.setHpDate(date);
				hph.setHpType("충전");
				hph.setPrice(1000);
				hph.setHolopayComment("충전");
				hph.setTradeId(123);
				hph.setMemberFinanceId(456);
			} else if (rsms.contains("잔액")) {
				String msg = rsms;
			}

		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
	}
}
