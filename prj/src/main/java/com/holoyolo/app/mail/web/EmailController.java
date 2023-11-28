package com.holoyolo.app.mail.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.holoyolo.app.mail.service.EmailService;
import com.holoyolo.app.mail.service.EmailVO;
import com.holoyolo.app.member.service.MemberVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EmailController {

	@Autowired
	private final EmailService emailService;
	
	
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
	
}
