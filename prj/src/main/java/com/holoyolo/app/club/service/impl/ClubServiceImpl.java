package com.holoyolo.app.club.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.club.mapper.ClubMapper;
import com.holoyolo.app.club.service.ClubService;
import com.holoyolo.app.club.service.ClubVO;

@Service
public class ClubServiceImpl implements ClubService {
	
	@Autowired
	ClubMapper clubMapper;

	// 모임 전체조회
	@Override
	public List<ClubVO> selectClubAll() {
		return clubMapper.selectClubAll();
	}

	// 모임 단건조회
	@Override
	public ClubVO selectClubInfo(ClubVO clubVO) {
		return clubMapper.selectClubInfo(clubVO);
	}
	
	// 모임 등록
	@Override
	public int insertClubInfo(ClubVO clubVO) {
		int result = clubMapper.insertClubInfo(clubVO);
		
		if (result == 1) {
			return clubVO.getClubId();
		} else {
			return -1;
		}
	}
	
	// 모임 수정
	@Override
	public Map<String, Object> updateClubInfo(ClubVO clubVO) {
		return null;
	}
	
	// 모임 삭제
	@Override
	public boolean deleteClubInfo(int clubId) {
		return false;
	}
}
