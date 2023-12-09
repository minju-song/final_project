package com.holoyolo.app.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.holoyolo.app.auth.provider.GoogleUserInfo;
import com.holoyolo.app.auth.provider.KakaoUserInfo;
import com.holoyolo.app.auth.provider.NaverUserInfo;
import com.holoyolo.app.auth.provider.OAuth2UserInfo;
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
		Map<String, Object> map = (Map)oauth2User.getAttributes();
		System.out.println("oauth2User : " + oauth2User.getAttributes().get("id"));
		System.out.println("map : " + map);
		System.out.println("map : " + map.get("properties"));
		System.out.println("map : " + map.get("kakao_account"));
		
		OAuth2UserInfo oauth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			oauth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			oauth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
		 	oauth2UserInfo = new KakaoUserInfo((Map)oauth2User.getAttributes());
		}
		
		String provider = oauth2UserInfo.getProvider();
		String providerId = oauth2UserInfo.getProviderId();
		String nickName = provider + "_" + providerId;
		String memberId = oauth2UserInfo.getEmail();
		String memberName = oauth2UserInfo.getName();
		String password = bCryptPasswordEncoder.encode("snslogin");
		String role = "ROLE_HA1";
		String snsYn = "Y";
		
		MemberVO member = memberService.selectUser(memberId);
		
		if(member == null) {
			System.out.println("가입진행");
			member = new MemberVO();
			
			member.setMemberId(memberId);
			member.setMemberName(memberName);
			member.setNickname(nickName);
			member.setPassword(password);
			member.setRole(role);
			member.setSnsYn(snsYn);
			
			memberService.joinUser(member);
		}
		
		return new PrincipalDetails(member, oauth2User.getAttributes());
	}

}
