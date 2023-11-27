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
import com.holoyolo.app.holopayHistory.service.api.HolopayWithdrawalApiVO;

@Service
public class HolopayWithdrawApi {
	private static final String REQUEST_URL = "https://developers.nonghyup.com/ReceivedTransferAccountNumber.nh";

	private final RestTemplate restTemplate;

	@Autowired
	HoloPayHistoryService holoPayHistoryService;

	public HolopayWithdrawApi(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public JSONObject getPosts(HolopayWithdrawalApiVO apiRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Gson gson = new Gson();
		String jsonReq = gson.toJson(apiRequest);
		HttpEntity<String> requestEntity = new HttpEntity<>(jsonReq, headers);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(REQUEST_URL, requestEntity, String.class);

		String responseBody = responseEntity.getBody();

		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
			return jsonObject;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}
}
