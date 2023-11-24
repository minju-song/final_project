package com.holoyolo.app.holopayHistory.web;

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
	private static final String REQUEST_URL = "https://developers.nonghyup.com/InquireCreditCardAuthorizationHistory.nh";

	private final RestTemplate restTemplate;

	@Autowired
	HoloPayHistoryService holoPayHistoryService;

	public HolopayRechargeApi(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void getPosts() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HolopayReqVO request = new HolopayReqVO();

		Gson gson = new Gson();
		String jsonReq = gson.toJson(request);

		HttpEntity<String> requestEntity = new HttpEntity<>(jsonReq, headers);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(REQUEST_URL, requestEntity, String.class);

	}
}
