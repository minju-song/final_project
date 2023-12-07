package com.holoyolo.app.clubSuccessHistoryMember.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.clubMember.service.ClubMemberVO;
import com.holoyolo.app.clubSuccessHistoryMember.service.ClubSuccessHistoryMemberService;

@Controller
public class ClubSuccessHistoryMemberController {
	
	@Autowired
	ClubSuccessHistoryMemberService clubSuccessHistoryMemberService;
	
	@GetMapping("/clubSuccessMember5")
	@ResponseBody
	public Map<String,Object> clubSuccessMember5(ClubMemberVO vo) {
		
		
		System.out.println(vo);
		
		
		return clubSuccessHistoryMemberService.getFiveHistory(vo);
	}
}
