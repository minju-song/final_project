package com.holoyolo.app.clubSuccessHistoryMember.mapper;

import java.util.List;

import com.holoyolo.app.clubMember.service.ClubMemberVO;
import com.holoyolo.app.clubSuccessHistoryMember.service.ClubSuccessHistoryMemberVO;

public interface ClubSuccessHistoryMemberMapper {

	//해당 회원의 지난 순위 가져오기
	public ClubMemberVO getRankingLast(ClubMemberVO vo);
	
	//해당 회원의 지난 클럽성적
	public List<ClubSuccessHistoryMemberVO> getSuccessMember(ClubMemberVO vo);
}
