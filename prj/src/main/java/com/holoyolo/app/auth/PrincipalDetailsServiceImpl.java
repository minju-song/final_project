package com.holoyolo.app.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;

//시큐리티 설정에서 .loginProcessingUrl("/login");
///login 요청이 오면 자동으로 UserDetailsService 타입으로 IOC되어 있는 loadUserByUsername함수가 실행
@Service
public class PrincipalDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private MemberService memberService;

	// 시큐리티 session <= Authentication <= UserDetails
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberVO user = memberService.selectUser(username);
		if(user != null) {
			return new PrincipalDetails(user);
		}
		return null;
	}

}
