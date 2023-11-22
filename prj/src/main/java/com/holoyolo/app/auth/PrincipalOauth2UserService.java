package com.holoyolo.app.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.holoyolo.app.config.CustomBCryptPasswordEncoder;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	private CustomBCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	MemberService memberService;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("userRequest : " + userRequest);
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		
		String provider = userRequest.getClientRegistration().getRegistrationId();
		String providerId = oauth2User.getAttribute("sub");
		String nickName = provider + "_" + providerId;
		String memberId = oauth2User.getAttribute("email");
		String memberName = oauth2User.getAttribute("name");
		String password = bCryptPasswordEncoder.encode("snslogin");
		System.out.println(password);
		String role = "HA1";
		
		MemberVO memberVO = memberService.selectUser(memberId);
		
		if(memberVO == null) {
			System.out.println("가입진행");
			memberVO = new MemberVO();
			
			memberVO.setMemberId(memberId);
			memberVO.setMemberName(memberName);
			memberVO.setNickname(nickName);
			memberVO.setPassword(password);
			memberVO.setRole(role);
			
			memberService.joinUser(memberVO);
		}
		
		
		
		return new PrincipalDetails(memberVO, oauth2User.getAttributes());
	}

}
