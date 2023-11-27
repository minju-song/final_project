package com.holoyolo.app.club.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.club.mapper.ClubMapper;
import com.holoyolo.app.club.service.ClubService;
import com.holoyolo.app.club.service.ClubVO;

@Service
public class ClubServiceImpl implements ClubService {
	
	@Autowired
	ClubMapper clubMapper;

	@Override
	public List<ClubVO> getAllClubList() {
		return clubMapper.getAllClubList();
	}
	
	@Override
	public List<ClubVO> getClubList(ClubVO vo) {
		return clubMapper.getClubList(vo);
	}


}
