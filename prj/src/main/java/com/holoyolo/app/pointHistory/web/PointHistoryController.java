package com.holoyolo.app.pointHistory.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.pointHistory.service.PointHistoryService;

@Controller
public class PointHistoryController {
	
	@Autowired
	PointHistoryService pointHistoryService;

	//마이페이지 포인트내역 페이지
	@GetMapping("/member/myPoint")
	public String myPointPage(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		Map<String, Object> map = pointHistoryService.pageMyPoint(principalDetails.getMemberVO());
		
		model.addAttribute("result", map);
		model.addAttribute("user", principalDetails.getMemberVO());
		model.addAttribute("menu", "mypage");
		return "user/mypage/myPoint";
	}
}
