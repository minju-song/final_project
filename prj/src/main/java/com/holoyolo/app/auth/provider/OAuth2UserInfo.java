package com.holoyolo.app.auth.provider;

public interface OAuth2UserInfo {
	
	String getProviderId();
	String getProvider();
	String getEmail();
	String getName();

}
