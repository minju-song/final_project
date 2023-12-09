package com.holoyolo.app.clubSuccessHistory.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.club.service.ClubService;
import com.holoyolo.app.club.service.ClubVO;
import com.holoyolo.app.clubSuccessHistory.service.ClubSuccessHistoryService;

@Controller
public class ClubSuccessHistoryController {
	
	@Autowired
	ClubSuccessHistoryService clubSuccessHistoryService;
	
	@Autowired
	ClubService clubService;
	
	//클럽 성공기록
	@GetMapping("/member/club/clubHistory")
	public String clubHistoryPage(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model,ClubVO vo) {
		Map<String, Object> map = new HashMap<>();
		System.out.println(vo);
		
		map = clubSuccessHistoryService.clubHistoryPage(vo);
		
		model.addAttribute("result", map);
		model.addAttribute("menu", "club");
		model.addAttribute("subMenu", "history");
		return "user/club/clubHistory";
	}
}
