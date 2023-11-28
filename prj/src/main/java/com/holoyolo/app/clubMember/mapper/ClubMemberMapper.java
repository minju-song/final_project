package com.holoyolo.app.clubMember.mapper;

import java.util.List;

import com.holoyolo.app.clubMember.service.ClubMemberVO;

public interface ClubMemberMapper {
	
	//해당 회원이 가입한 클럽 리스트
	public List<ClubMemberVO> getClubJoin(ClubMemberVO vo);
	
	//즉시 클럽가입
	public int joinClub(ClubMemberVO vo);
}
