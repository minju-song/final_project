package com.holoyolo.app.heart.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.heart.service.HeartService;
import com.holoyolo.app.heart.service.HeartVO;

@Controller
public class HeartController {
	@Autowired
	HeartService heartService;
	
	@PostMapping("member/heartInsert")
	@ResponseBody
	public int heartInsert(@AuthenticationPrincipal PrincipalDetails principalDetails, 
							  HeartVO heartVO) {
		System.out.println(heartVO);
		heartVO.setMemberId(principalDetails.getUsername());
		int id = heartService.insertHeart(heartVO);
		return id;
	}
	
	@GetMapping("member/heartDelete")
	@ResponseBody
	public void heartDelete(@AuthenticationPrincipal PrincipalDetails principalDetails, 
			  				HeartVO heartVO) {
		heartVO.setMemberId(principalDetails.getUsername());
		heartService.deleteHeart(heartVO);
	}
}
