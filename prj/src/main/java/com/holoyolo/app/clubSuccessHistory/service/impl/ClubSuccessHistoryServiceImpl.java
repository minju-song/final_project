package com.holoyolo.app.clubSuccessHistory.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.club.mapper.ClubMapper;
import com.holoyolo.app.club.service.ClubVO;
import com.holoyolo.app.clubSuccessHistory.mapper.ClubSuccessHistoryMapper;
import com.holoyolo.app.clubSuccessHistory.service.ClubSuccessHistoryService;
import com.holoyolo.app.clubSuccessHistory.service.ClubSuccessHistoryVO;

@Service
public class ClubSuccessHistoryServiceImpl implements ClubSuccessHistoryService {
	
	@Autowired
	ClubSuccessHistoryMapper clubSuccessHistoryMapper;

	@Autowired
	ClubMapper clubMapper;
	
	@Override
	public Map<String, Object> clubHistoryPage(ClubVO vo) {
		Map<String, Object> map = new HashMap<>();
		
		//해당 클럽정보
		vo = clubMapper.getClub(vo);
		
		//해당 클럽 성공기록
		List<ClubSuccessHistoryVO> list = clubSuccessHistoryMapper.getClubSuccess(vo.getClubId());
		
		
		map.put("list", list);
		map.put("club", vo);
		return map;
	}

}
