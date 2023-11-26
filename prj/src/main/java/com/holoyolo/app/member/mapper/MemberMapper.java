package com.holoyolo.app.member.mapper;

import com.holoyolo.app.member.service.MemberVO;

public interface MemberMapper {
	
	//로그인요청온 회원 찾기
	public MemberVO selectUser(String username);
	
	//회원가입
	public int joinUser(MemberVO memberVO);

	//회원가입날짜 조회
	public MemberVO selectJoinDate(MemberVO vo);
	
	//회원가입 - 아이디 중복 체크
	public MemberVO checkMemberId(MemberVO memberVO);
	
	//회원가입 - 닉네임 중복 체크
	public MemberVO checkNickname(MemberVO memberVO);
}
