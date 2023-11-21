package com.holoyolo.app.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	MemberService memberService;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		System.out.println(oAuth2User.getAttributes());
		
		String memberId = oAuth2User.getAttribute("email");
		System.out.println("memberId : " + memberId);
		String memberName = oAuth2User.getAttribute("name");
		String password = "snslogin";
		String role = "HA1";
		
		MemberVO memberVO = memberService.selectUser(memberId);
		
		memberVO.setMemberId(memberId);
		memberVO.setMemberName(memberName);
		memberVO.setPassword(password);
		memberVO.setRole(role);
		
//		if(memberVO == null) {
//			memberVO.setMemberId(memberId);
//			memberVO.setMemberName(memberName);
//			memberVO.setPassword(password);
//			memberVO.setRole(role);
//			
//			memberService.joinUser(memberVO);
//		}
		
		return new PrincipalDetails(memberVO, oAuth2User.getAttributes());
	}

}
