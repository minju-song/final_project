package com.holoyolo.app.member.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	

	
	/**
	 * 로그인 페이지
	 * @return
	 */
	@GetMapping("/loginForm")
	public String loginForm(@RequestParam(value="error", required = false) String error,
				            @RequestParam(value="exception", required = false) String exception,
				            Model model) {
		
		model.addAttribute("error", error);
		model.addAttribute("exception", exception);
		
		return "/user/loginForm";
	}
	
	/**
	 * 회원가입 페이지
	 * @return
	 */
	@GetMapping("/joinForm")
	public String joinForm() {
		return "/user/joinForm";
	}
	
	/**
	 * 회원가입
	 * @param memberVO
	 * @return
	 */
	@PostMapping("/join")
	@ResponseBody
	public String join(MemberVO memberVO) {
		System.out.println(memberVO);
		
		MemberVO vo = memberService.checkUserPhone(memberVO);
		if(vo != null) {
			System.out.println(vo.getPhone() + " :: 이미 가입된 회원입니다.");
			return "JoinUser";
		} else {
			String rawPassword = memberVO.getPassword();
			String encPassword = passwordEncoder.encode(rawPassword);
			memberVO.setPassword(encPassword);
			memberVO.setRole("HA1"); //일반회원
			
			System.out.println(memberVO);
			
			int result = memberService.joinUser(memberVO);
			
			if(result > 0) {
				return "Success";
			} else {
				return "Fail";
			}
		}
	}
	
	/**
	 * 아이디 중복체크
	 * @param memberVO
	 * @return
	 */
	@GetMapping("/join/idCheck")
	@ResponseBody
	public String idCheck(MemberVO memberVO) {
		String result = "NOT_FOUND";
		MemberVO vo = new MemberVO();
		System.out.println("넘어온 아이디 ::: " + memberVO.getMemberId());
		
		vo = memberService.checkMemberId(memberVO);
		System.out.println(memberVO.getMemberId() + "는 가입이 가능한 아이디 입니다.");
		
		if(vo != null) {
			result = "FOUND";
			System.out.println("결과는 ::: " + vo.getMemberId() + ", " + result);
		}
		
		return result;
	}
	
	/**
	 * 닉네임 중복체크
	 * @param memberVO
	 * @return
	 */
	@GetMapping("/join/nickCheck")
	@ResponseBody
	public String nickCheck(MemberVO memberVO) {
		String result = "NOT_FOUND";
		MemberVO vo = new MemberVO();
		System.out.println("넘어온 닉네임 ::: " + memberVO.getNickname());
		
		vo = memberService.checkNickname(memberVO);
		System.out.println(memberVO.getNickname() + "는 사용이 가능한 닉네임 입니다.");
		
		if(vo != null) {
			result = "FOUND";
			System.out.println("결과는 ::: " + vo.getNickname() + ", " + result);
		}
		
		return result;
	}
	
	/**
	 * 아이디/비밀번호 찾기 페이지
	 * @return
	 */
	@GetMapping("/findForm")
	public String findForm() {
		return "/user/findForm";
	}
	
	/**
	 * 아이디 찾기
	 * @param memberVO
	 * @return
	 */
	@GetMapping("/find")
	@ResponseBody
	public String findMemberId(MemberVO memberVO) {
		MemberVO vo = new MemberVO();
		vo = memberService.findMemberIdPwd(memberVO);
		System.out.println("조회된 결과:: " + vo);
		
		if(vo != null) {
			return vo.getMemberId();
		} else {
			return "Fail";
		}
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
			return "redirect:/member/myInfo";
		}
		return "redirect:/";
	}
	
	/**
	 * 마이페이지-홈
	 * @param principalDetails
	 * @param model
	 * @return
	 */
	@GetMapping("/member/myHome")
	public String myHome(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		String memberId = principalDetails.getUsername();
		MemberVO memberVO = memberService.selectUser(memberId);
		model.addAttribute("memberInfo", memberVO);
		
		// 사이드메뉴 정보 넘기기
		model.addAttribute("menu", "mypage");
		model.addAttribute("subMenu", "myHome");
		
		return "user/mypage/myHome";
	}
	
	/**
	 * 마이페이지-내정보
	 * @param principalDetails
	 * @param model
	 * @return
	 */
	@GetMapping("/member/myInfo")
	public String myInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		String memberId = principalDetails.getUsername();
		MemberVO memberVO = memberService.selectUser(memberId);
		model.addAttribute("memberInfo", memberVO);
		
		// 사이드메뉴 정보 넘기기
		model.addAttribute("menu", "mypage");
		model.addAttribute("subMenu", "myInfo");
		
		return "user/mypage/myInfo";
	}
	
	@GetMapping("/mail/test")
	public String mailTest() {
		return "/user/mailbody/password";
	}
	
}