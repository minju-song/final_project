package com.holoyolo.app.clubSuccessHistoryMember.mapper;

import com.holoyolo.app.clubMember.service.ClubMemberVO;

public interface ClubSuccessHistoryMemberMapper {

	//해당 회원의 지난 순위 가져오기
	public ClubMemberVO getRankingLast(ClubMemberVO vo);
}
