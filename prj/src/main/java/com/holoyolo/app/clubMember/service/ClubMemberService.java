package com.holoyolo.app.clubMember.service;

import java.util.List;

import com.holoyolo.app.club.service.ClubVO;

public interface ClubMemberService {
	
	//해당 회원이 가입한 클럽 리스트
	public List<ClubMemberVO> getClubJoin(String id);
	
	//즉시 클럽가입
	public int joinClub(ClubMemberVO vo);
	
	//내가 가입한 클럽인지 확인
	public ClubMemberVO checkMyClub(ClubMemberVO vo);
	

	//클럽가입신청
	public int reqClub(ClubMemberVO vo);
	
	//클럽가입승인
	public String acceptClub(ClubMemberVO vo);
	
	//클럽탈퇴
	public int outClubMember(ClubMemberVO vo);
	
	//클럽재가입
	public int reJoin(ClubMemberVO vo);
	
	//클럽재가입신청
	public int reqRejoin(ClubMemberVO vo);
}
