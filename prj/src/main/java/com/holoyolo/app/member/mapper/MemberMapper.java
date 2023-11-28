package com.holoyolo.app.member.mapper;

import java.util.List;
import java.util.Map;

import com.holoyolo.app.member.service.MemberVO;

public interface MemberMapper {
	
	// 로그인요청 온 회원 찾기
	public MemberVO selectUser(String username);
	
	// 회원 가입
	public int joinUser(MemberVO memberVO);

	// 회원 가입일자 조회
	public MemberVO selectJoinDate(MemberVO vo);
	

	// 기본 CRUD : 공성훈 추가
	// 회원 전체조회
	public List<MemberVO> selectMemberAll();
	
	// 회원 단건조회
	public MemberVO selectMemberInfo(MemberVO memberVO);
	
	// 회원 수정
	public Map<String, Object> updateMemberInfo(MemberVO memberVO);
	
	// 회원 정지
	
	//회원가입 - 아이디 중복 체크
	public MemberVO checkMemberId(MemberVO memberVO);
	
	//회원가입 - 닉네임 중복 체크
	public MemberVO checkNickname(MemberVO memberVO);

}
