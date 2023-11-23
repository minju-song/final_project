package com.holoyolo.app.member.service;

import java.util.Date;

public interface MemberService {
	
	//로그인요청온 회원 찾기
	public MemberVO selectUser(String username);
	
	//회원가입
	public int joinUser(MemberVO memberVO);

	//회원가입날짜 조회
	public Date selectJoinDate(String id);
}
