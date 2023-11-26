package com.holoyolo.app.holopayHistory.web;

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

import com.google.gson.Gson;
import com.holoyolo.app.holopayHistory.service.HoloPayHistoryService;
import com.holoyolo.app.holopayHistory.service.HolopayReqVO;

@Service
public class HolopayRechargeApi {
	private static final String REQUEST_URL = "https://developers.nonghyup.com/DrawingTransfer.nh";

	private final RestTemplate restTemplate;
	
	@Autowired
	HoloPayHistoryService holoPayHistoryService;

	public HolopayRechargeApi(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void getPosts(HolopayReqVO apiRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
System.out.println(apiRequest);
		Gson gson = new Gson();
		String jsonReq = gson.toJson(apiRequest);

		HttpEntity<String> requestEntity = new HttpEntity<>(jsonReq, headers);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(REQUEST_URL, requestEntity, String.class);

		String responseBody = responseEntity.getBody();
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(responseBody);

			System.out.println(jsonObject);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
