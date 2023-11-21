package com.holoyolo.app.member.mapper;

import com.holoyolo.app.member.service.MemberVO;

public interface MemberMapper {
	
	//로그인요청온 회원 찾기
	public MemberVO selectUser(String username);
	
	//회원가입
	public int joinUser(MemberVO memberVO);

}
