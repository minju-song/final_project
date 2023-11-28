package com.holoyolo.app.clubMember.web;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.clubMember.service.ClubMemberService;
import com.holoyolo.app.clubMember.service.ClubMemberVO;

@Controller
public class ClubMemberController {
	
	@Autowired
	ClubMemberService clubMemberService;
	
	@GetMapping("/joinClub")
	@ResponseBody
	public String joinClub(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestParam(name = "clubId") String clubId) {
		//결과맵
		
		if(principalDetails == null) {
			return "redirect:/loginForm";
		}
		System.out.println("실행완");
//		Map<String, Object> map = new HashMap<>();
		ClubMemberVO vo =new ClubMemberVO();
		vo.setMemberId(principalDetails.getUsername());

		vo.setClubId(Integer.valueOf(clubId));
		if(clubMemberService.joinClub(vo) > 0 ) {
			return "success";
		}
		else {
			return "fail";
		}
	}
}
