package com.holoyolo.app.clubSuccessHistoryMember.service;

import java.util.Map;

import com.holoyolo.app.clubMember.service.ClubMemberVO;

public interface ClubSuccessHistoryMemberService {
	
	//클럽의 회원 최근 5회 성적
	public Map<String, Object> getFiveHistory (ClubMemberVO vo);

}
