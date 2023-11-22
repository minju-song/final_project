package com.holoyolo.app.member.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
		System.out.println("회원아이디 : " + principalDetails.getUsername());
		return "세션정보 로그로 확인 바람";
	}
	
	/**
	 * 로그인 페이지
	 * @return
	 */
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	/**
	 * 회원가입 페이지
	 * @return
	 */
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	/**
	 * 회원가입
	 * @param memberVO
	 * @return
	 */
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
	
	/**
	 * 소셜 최초로그인시 마이페이지로 이동.
	 * @param principalDetails
	 * @param model
	 * @return
	 */
	@GetMapping("/oauth2/callback")
	public String oauth2joinForm(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		MemberVO memberInfo = principalDetails.getMemberVO();
		
		if(memberInfo.getAddr() == null) {
			return "redirect:/member/memberInfo";
		}
		return "redirect:/";
	}
	
	@GetMapping("/member/memberInfo")
	public String memberInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		String memberId = principalDetails.getUsername();
		MemberVO memberVO = memberService.selectUser(memberId);
		model.addAttribute("memberInfo", memberVO);
		model.addAttribute("subMenu", "memberInfo");
		
		return "user/mypage/memberInfo";
	}
	
	
	
}