package com.holoyolo.app.mail.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.club.service.ClubVO;
import com.holoyolo.app.clubMember.service.ClubMemberService;
import com.holoyolo.app.clubMember.service.ClubMemberVO;
import com.holoyolo.app.mail.service.EmailService;
import com.holoyolo.app.mail.service.EmailVO;
import com.holoyolo.app.member.service.MemberVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EmailController {

	@Autowired
	private final EmailService emailService;
	
	@Autowired
	private final ClubMemberService clubMemberService;
	
	
	/**
	 * 임시 비밀번호 발급
	 * @param email
	 * @return
	 */
	@PostMapping("/sendmail/password")
	public void sendPasswordMail(MemberVO memberVO) {
		EmailVO emailVO = EmailVO.builder()
				.to(memberVO.getMemberId())
				.subject("[holoyolo] 임시 비밀번호 발급 메일입니다.")
				.build();
		
		emailService.sendMail(emailVO, "password");
	}
	
	/**
	 * 회원가입 축하메일
	 * @param memberVO
	 * @return
	 */
	@PostMapping("/sendmail/joinmail")
	public void sendJoinMail(MemberVO memberVO) {
		EmailVO emailVO = EmailVO.builder()
				.to(memberVO.getMemberId())
				.subject("[holoyolo]" + memberVO.getMemberName() + "님, 회원가입을 축하드립니다")
				.build();
		
		emailService.sendMail(emailVO, "join");
	}
	
	//클럽가입요청
	//@RequestMapping(value = "/sendmail/requestclub", method = RequestMethod.POST)
	@PostMapping("/sendmail/requestclub")
	public void sendRequestMail(@AuthenticationPrincipal PrincipalDetails principalDetails,@RequestBody ClubVO vo) {
		EmailVO emailVO = EmailVO.builder()
				.to("songjaeskkk@naver.com")
				.subject(vo.getClubName()+"모임에 대한 가입요청 메일입니다.")
				.clubId(vo.getClubId())
				.clubName(vo.getClubName())
				.reqId(principalDetails.getUsername())
				.text(vo.getText())
				.build();
		
		System.out.println("이메일객체"+vo);
		
		ClubMemberVO cmvo = new ClubMemberVO();
		cmvo.setClubId(vo.getClubId());
		cmvo.setMemberId(principalDetails.getUsername());
		
		if(clubMemberService.reqClub(cmvo) > 0) {
			System.out.println("성공");
		}
		else {
			System.out.println("실패");
		}
		emailService.sendRequest(emailVO);
	}
	
	
	
}
