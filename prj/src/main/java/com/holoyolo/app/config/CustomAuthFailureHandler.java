package com.holoyolo.app.config;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;

@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	MemberService memberService;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		MemberVO memberVO = new MemberVO();
		String username = request.getParameter("memberId");
		memberVO.setMemberId(username);
		System.out.println(username);
		System.out.println(memberVO);
		
		String errorMessage;
		if (exception instanceof BadCredentialsException) {
			errorMessage = "아이디 또는 비밀번호가 맞지 않습니다, 5회 실패시 계정이 잠금됩니다.";
			int result = loginFailure(memberVO);
			System.out.println("실패횟수 업데이트 :: " + result);
			
		} else if (exception instanceof InternalAuthenticationServiceException) {
			errorMessage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
		} else if (exception instanceof UsernameNotFoundException) {
			errorMessage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
		} else if (exception instanceof AuthenticationCredentialsNotFoundException) {
			errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
		} else {
			errorMessage = "[계정잠금] 비밀번호 찾기 후 다시 시도 해주세요.";
		}
		errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
		setDefaultFailureUrl("/loginForm?error=true&exception="+errorMessage);
		
		
		super.onAuthenticationFailure(request, response, exception);
	}
	
	/**
	 * 로그인 실패횟수 ++
	 * @param memberVO
	 */
	protected int loginFailure(MemberVO memberVO) {
		System.out.println(memberVO);
		int result = memberService.updateMemberFailCnt(memberVO);
		return result;
	}
	
	
}
