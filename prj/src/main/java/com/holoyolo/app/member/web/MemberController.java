package com.holoyolo.app.member.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;

@Controller
public class MemberController {
	
	@Autowired
	MemberService memberService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/user")
	public @ResponseBody String user() {
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}

	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(MemberVO memberVO) {
		String rawPassword = memberVO.getPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		memberVO.setPassword(encPassword);
		memberVO.setRole("HA1"); //일반회원
		
		System.out.println(memberVO);
		
		memberService.joinUser(memberVO);
		
		return "redirect:/loginForm";
	}

}
