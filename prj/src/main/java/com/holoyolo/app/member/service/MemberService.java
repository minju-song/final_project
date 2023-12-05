package com.holoyolo.app.member.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
	
	// 로그인요청 온 회원 찾기
	public MemberVO selectUser(String username);
	
	// 회원 가입
	public String joinUser(MemberVO memberVO);

	// 회원 가입일자조회
	//public Date selectJoinDate();
	
	// 기본 CRUD : 공성훈 추가
	// 회원 전체조회
	public List<MemberVO> selectMemberAll(MemberVO memberVO);
	
	// 회원 단건조회
	public MemberVO selectMemberInfo(MemberVO memberVO);
	
	// 회원정보 업데이트
	public boolean updateMemberInfo(MemberVO memberVO);
	
	// 회원 정지

	// 회원가입날짜 조회
	public Date selectJoinDate(String id);
	
	// 회원가입 - 아이디 중복 체크
	public String checkMemberId(MemberVO memberVO);
	
	// 회원가입 - 닉네임 중복 체크
	public String checkNickname(MemberVO memberVO);
	
	// 아이디/비밀번호 찾기
	public String findMemberIdPwd(MemberVO memberVO);
	
	// 임시비밀번호 업데이트
	public int updateMemberPwd(String memberId, String authNum);
	
	// 로그인 실패횟수 ++
	public int updateMemberFailCnt(MemberVO memberVO);
	
	// 로그인 실패횟수 초기화
	public int updateMemberFailCntReset(MemberVO memberVO);
	
	// 회원 프로필 사진 수정
	public String uploadImage(MultipartFile file, String memberId);

	public Object selectMemberCount(MemberVO memberVO);

	
	// 휴대폰 변경-이미 사용중인 번호인지 체크
	public boolean phoneCheck(MemberVO memberVO);
	
	// 회원탈퇴
	public boolean deleteMember(MemberVO memberVO);
	
	// 비밀번호 검증
	public boolean checkPassword(MemberVO memberVO);
	
}
