package com.holoyolo.app.club.mapper;

import java.util.List;

import com.holoyolo.app.club.service.ClubVO;

public interface ClubMapper {
	
	//알뜰모임 목록
	public List<ClubVO> getAllClubList();
	
	//알뜰모임 목록 페이징
	public List<ClubVO> getClubList(ClubVO vo);
}
