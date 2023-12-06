package com.holoyolo.app.chat.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.chat.service.ChatRoomVO;
import com.holoyolo.app.chat.service.ChatService;
import com.holoyolo.app.chat.service.ChatVO;
import com.holoyolo.app.club.service.ClubService;
import com.holoyolo.app.club.service.ClubVO;

@Controller
public class ChatController {

	@Autowired
	ClubService clubService;
	
	@Autowired
	ChatService chatService;

	
	@GetMapping("/member/club/clubChat")
	public String clubChatting(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model, ClubVO vo) {
		Map<String, Object> map = new HashMap<>();
		vo = clubService.getClub(vo.getClubId());
		map.put("club", vo);
		System.out.println("채팅방입장");
		
		ChatRoomVO room = chatService.findOrCreateRoom(vo.getClubId());

		
		
		System.out.println(room.getClubId());
		System.out.println(principalDetails.getMemberVO().getMemberName());
		
		model.addAttribute("room", room);
		model.addAttribute("memberId", principalDetails.getUsername());
		model.addAttribute("memberName", principalDetails.getMemberVO().getMemberName());
		model.addAttribute("result", map);
		model.addAttribute("menu", "club");
		model.addAttribute("subMenu", "chat");
		
		
		return "user/club/clubChat";
	}
	
	@PostMapping("/member/insertChat")
	@ResponseBody
	public Map<String, Object> insertChat(@RequestBody String str){
		Map<String, Object> map = new HashMap<>();
		System.out.println("db저장값 : "+str);

		if(chatService.insertChat(str) > 0) {
			map.put("result", "success");
		}
		else {
			map.put("result", "fail");
		}
		return map;
	}
	
}
