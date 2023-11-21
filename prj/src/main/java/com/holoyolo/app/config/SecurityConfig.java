package com.holoyolo.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.holoyolo.app.auth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터(SecurityConfig)가 스프링 필터체인에 등록됨.
public class SecurityConfig {
	
	@Autowired
	PrincipalOauth2UserService principalOauth2UserService;

	@Bean // 리턴되는 객체를 IOC로 등록..
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
	        .antMatchers("/adminhome/**").access("hasRole('ROLE_ADMIN')")
	        .antMatchers("/member/trade/**").access("hasRole('HA1')")
	        .antMatchers("/member/**").authenticated() // 인증만되면 들어갈 수 있는 주소
	        .anyRequest().permitAll()
	        .and()
	        .formLogin()
	        .loginPage("/loginForm")
	        .usernameParameter("memberId")
	        .loginProcessingUrl("/login") // /login 호출시 시큐리티가 인터셉트해서 대신 로그인을 진행..
	        .defaultSuccessUrl("/")
	        .and()
	        .oauth2Login()
	        .loginPage("/loginForm")
	        .userInfoEndpoint()
	        .userService(principalOauth2UserService); //구글로그인 완료후 후처리 필요
        return http.build();
    }
}
