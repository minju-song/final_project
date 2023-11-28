package com.holoyolo.app.clubMember.service;

import java.util.List;

public interface ClubMemberService {
	
	//해당 회원이 가입한 클럽 리스트
	public List<ClubMemberVO> getClubJoin(String id);
	
	//즉시 클럽가입
	public int joinClub(ClubMemberVO vo);
}
