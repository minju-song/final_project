package com.holoyolo.app.member.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;

@Controller
public class MemberController {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;


	@GetMapping("/member")
	public @ResponseBody String member() {
		return "member";
	}
	
	@GetMapping("/session")
	public @ResponseBody String sessionTest(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("==== 세션정보 확인 ====");
		System.out.println("세션정보 : " + principalDetails.getMemberVO());
		return "세션정보 로그로 확인 바람";
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
