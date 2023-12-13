package com.holoyolo.app.member.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.holoyolo.app.accBookHistory.service.AccBookHistoryService;
import com.holoyolo.app.accBookHistory.service.AccBookHistoryVO;
import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.holopayHistory.service.HoloPayHistoryService;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;
import com.holoyolo.app.pointHistory.service.PointHistoryService;

@Controller
public class MemberController {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	AccBookHistoryService accBookHistoryService;
	
	@Autowired
	HoloPayHistoryService holoPayHistoryService;
	
	@Autowired
	PointHistoryService pointHistoryService;
	
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
		
		return "user/loginForm";
	}
	
	/**
	 * 회원가입 페이지
	 * @return
	 */
	@GetMapping("/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}
	
	/**
	 * 회원가입
	 * @param memberVO
	 * @return
	 */
	@PostMapping("/join")
	@ResponseBody
	public String join(MemberVO memberVO) {
		String result = memberService.joinUser(memberVO);
		return result;
	}
	
	/**
	 * 아이디 중복체크
	 * @param memberVO
	 * @return
	 */
	@GetMapping("/join/idCheck")
	@ResponseBody
	public String idCheck(MemberVO memberVO) {
		String result = memberService.checkMemberId(memberVO);
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
		String result = memberService.checkNickname(memberVO);
		return result;
	}
	
	/**
	 * 아이디/비밀번호 찾기 페이지
	 * @return
	 */
	@GetMapping("/findForm")
	public String findForm() {
		return "user/findForm";
	}
	
	/**
	 * 아이디,비밀번호 찾기
	 * @param memberVO
	 * @return
	 */
	@GetMapping("/find")
	@ResponseBody
	public String findMemberIdPwd(MemberVO memberVO) {
		String result = memberService.findMemberIdPwd(memberVO);
		return result;
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
		
		if(memberInfo.getAddr() == null) { // 주소가 null이면 이동
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
		
		// 회원의 개인정보
		MemberVO memberVO = memberService.selectUser(memberId);
		model.addAttribute("memberInfo", memberVO);
		
		// 회원 홀로페이, 포인트 잔액
		int hpBalance = holoPayHistoryService.holopayBalance(memberVO);
		int ptBalance = pointHistoryService.pointBalance(memberVO);
		model.addAttribute("hpBalance", hpBalance);
		model.addAttribute("ptBalance", ptBalance);
		
		
		// 회원의 가계부 데이터
		AccBookHistoryVO abvo = new AccBookHistoryVO();
		abvo.setMemberId(principalDetails.getUsername());
		// 현금 데이터
		abvo.setPaymentType("GA1");
		List<AccBookHistoryVO> cashData = accBookHistoryService.selectChartData(abvo);
		model.addAttribute("cashData", cashData);
		// 뱅킹 데이터
		abvo.setPaymentType("GA2");
		List<AccBookHistoryVO> bankData = accBookHistoryService.selectChartData(abvo);
		model.addAttribute("bankData", bankData);
		// 카드 데이터
		abvo.setPaymentType("GA3");
		List<AccBookHistoryVO> cardData = accBookHistoryService.selectChartData(abvo);
		model.addAttribute("cardData", cardData);
		
		// 개행처리
		String nlString = System.getProperty("line.separator").toString();
		model.addAttribute("nlString", nlString);
		
		// 사이드메뉴 정보 넘기기
		model.addAttribute("menu", "mypage");
		
		return "user/mypage/myHome";
	}
	
	/**
	 * 마이페이지-내정보 페이지
	 * @param principalDetails
	 * @param model
	 * @return
	 */
	@GetMapping("/member/myInfo")
	public String myInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		// 사이드메뉴 정보 넘기기
		model.addAttribute("menu", "mypage");
		model.addAttribute("subMenu", "myInfo");
		
		return "user/mypage/myInfo";
	}
	
	/**
	 * 마이페이지-내정보 페이지(인증화면)
	 * @return
	 */
	@GetMapping("/member/myInfo/authview")
	public String infoAuthView() {
		return "user/mypage/memberAuthView";
	}
	/**
	 * 마이페이지-내정보 페이지(인증)
	 * @return
	 */
	@PostMapping("/member/myInfo/auth")
	@ResponseBody
	public boolean infoAuth(HttpServletRequest request, MemberVO memberVO, Model model,
							@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		memberVO.setMemberId(principalDetails.getUsername());
		boolean result = memberService.checkPassword(memberVO);
		if(result == true) {
			model.addAttribute("authYn", "auth_Success");
		}
		
		return result;
	}
	/**
	 * 마이페이지-내정보 페이지(정보화면)
	 * @param principalDetails
	 * @param model
	 * @return
	 */
	@GetMapping("/member/myInfo/infoView")
	public String infoView(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		String memberId = principalDetails.getUsername();
		MemberVO memberVO = memberService.selectUser(memberId);
		model.addAttribute("memberInfo", memberVO);
		
		// 개행처리
		String nlString = System.getProperty("line.separator").toString();
		model.addAttribute("nlString", nlString);
		
		return "user/mypage/memberInfoView";
	}
	
	/**
	 * 마이페이지-내정보 : 프로필사진 업데이트
	 * @param principalDetails
	 * @param file
	 * @return
	 */
	@PostMapping("/member/myInfo/uploadImg")
	@ResponseBody
	public String uploadImage(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestPart MultipartFile file) {
		String result = memberService.uploadImage(file, principalDetails.getUsername());
		return result;
	}
	
	/**
	 * 마이페이지-내정보 : 업데이트
	 * @param memberVO
	 * @return
	 */
	@PostMapping("/member/myinfo/updateInfo")
	@ResponseBody
	public boolean updateNickname(@AuthenticationPrincipal PrincipalDetails principalDetails, MemberVO memberVO) {
		boolean result = false;
		memberVO.setMemberId(principalDetails.getUsername()); // 회원아이디 set
		
		// 회원정보 업데이트
		result = memberService.updateMemberInfo(memberVO);
		
		// 세션 정보 업데이트
		MemberVO vo = memberService.selectUser(memberVO.getMemberId());
		principalDetails.setMemberVO(vo);
		
		return result;
	}
	
	/**
	 * 마이페이지-내정보 : 휴대폰 업데이트 중복여부 체크
	 * @param principalDetails
	 * @param memberVO
	 * @return
	 */
	@PostMapping("/member/myinfo/phoneCheck")
	@ResponseBody
	public boolean phoneCheck(@AuthenticationPrincipal PrincipalDetails principalDetails, MemberVO memberVO) {
		memberVO.setMemberId(principalDetails.getUsername()); // 회원아이디 set
		return memberService.phoneCheck(memberVO);
	}
	
	/**
	 * 회원탈퇴 페이지
	 * @param model
	 * @return
	 */
	@GetMapping("/member/deleteForm")
	public String deleteMemberForm(Model model) {
		// 사이드메뉴 정보 넘기기
		model.addAttribute("menu", "mypage");
		return "user/mypage/memberDelete";
	}
	
	/**
	 * 회원탈퇴 처리
	 * @param principalDetails
	 * @param memberVO
	 * @return
	 */
	@PostMapping("/member/delete")
	@ResponseBody
	public boolean deleteMember(@AuthenticationPrincipal PrincipalDetails principalDetails, MemberVO memberVO) {
		memberVO.setMemberId(principalDetails.getUsername());
		boolean result = memberService.deleteMember(memberVO);
		
		return result;
	}
	
}