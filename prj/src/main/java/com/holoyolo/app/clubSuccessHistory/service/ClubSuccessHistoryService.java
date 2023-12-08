package com.holoyolo.app.clubSuccessHistory.service;

import java.util.Map;

import com.holoyolo.app.club.service.ClubVO;

public interface ClubSuccessHistoryService {
	
	//클럽성공기록페이지
	public Map<String, Object> clubHistoryPage(ClubVO vo);

}
