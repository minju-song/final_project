package com.holoyolo.app.holopayHistory.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class HoloPayController {

	@GetMapping("/myHolopay")
	public String myHoloPayPage() {
		return "member/mypage/holopay";
	}

	RestTemplate restTemplate;

	@Autowired
	public HoloPayController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping("/rechargeApi")
	public String rechargePay() {
		return "";

	}
}
