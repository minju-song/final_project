package com.holoyolo.app.auth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {
	
	private Map<String, Object> attributes; // oauth2User.getAttributes()
	
	public KakaoUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getProviderId() {
		return String.valueOf(attributes.get("id"));
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getEmail() {
		Map<String, Object> kakaoAccount = (Map)attributes.get("kakao_account");
		return (String) kakaoAccount.get("email");
	}

	@Override
	public String getName() {
		Map<String, Object> properties = (Map)attributes.get("properties");
		return (String) properties.get("nickname");
	}

}
