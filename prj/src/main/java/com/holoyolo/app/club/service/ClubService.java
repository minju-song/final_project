package com.holoyolo.app.club.service;

import java.util.List;

public interface ClubService {

	//알뜰모임 목록
	public List<ClubVO> getAllClubList();
	
	//알뜰모임 목록
	public List<ClubVO> getClubList(ClubVO vo);
}
