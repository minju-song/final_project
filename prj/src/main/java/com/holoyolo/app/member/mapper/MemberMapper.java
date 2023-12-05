package com.holoyolo.app.member.mapper;

import java.util.List;
import java.util.Map;

import com.holoyolo.app.member.service.MemberVO;

public interface MemberMapper {
	
	// 로그인요청 온 회원 찾기
	public MemberVO selectUser(String username);
	
	// 회원가입 전 인증된 휴대폰 번호가 있는지 확인
	public MemberVO checkUserPhone(MemberVO memberVO);
	
	// 회원 가입
	public int joinUser(MemberVO memberVO);

	// 회원 가입일자 조회
	public MemberVO selectJoinDate(MemberVO vo);
	
	// 회원 전체조회
	public List<MemberVO> selectMemberAll();
	
	// 회원 단건조회
	public MemberVO selectMemberInfo(MemberVO memberVO);
	
	// 회원정보 업데이트
	public int updateMemberInfo(MemberVO memberVO);
	
	// 회원 정지
	
	// 회원가입 - 아이디 중복 체크
	public MemberVO checkMemberId(MemberVO memberVO);
	
	// 회원가입 - 닉네임 중복 체크
	public MemberVO checkNickname(MemberVO memberVO);
	
	// 아이디/비밀번호 찾기
	public MemberVO findMemberIdPwd(MemberVO memberVO);
	
	// 임시비밀번호 업데이트
	public int updateMemberPwd(String memberId, String authNum);
	
	// 로그인 실패횟수 ++
	public int updateMemberFailCnt(MemberVO memberVO);

	// 로그인 실패횟수 초기화
	public int updateMemberFailCntReset(MemberVO memberVO);

	public List<MemberVO> selectMemberAll(MemberVO memberVO);

	public Object selectMemberCount(MemberVO memberVO);
}
