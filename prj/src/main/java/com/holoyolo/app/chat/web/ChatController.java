package com.holoyolo.app.chat.web;

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

@Controller
public class ChatController {

	@Autowired
	ClubService clubService;

	
	@GetMapping("/member/club/clubChat")
	public String clubChatting(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model, ClubVO vo) {
		Map<String, Object> map = new HashMap<>();
		vo = clubService.getClub(vo.getClubId());
		map.put("club", vo);
		System.out.println("채팅방입장");
		//해당클럽의 채팅내역 찾아오기
//		model.addAttribute("chat",chat);
		//model
		model.addAttribute("userId", principalDetails.getUsername());
		model.addAttribute("result", map);
		model.addAttribute("menu", "club");
		model.addAttribute("subMenu", "chat");
		
		return "user/club/clubChat";
	}
}
