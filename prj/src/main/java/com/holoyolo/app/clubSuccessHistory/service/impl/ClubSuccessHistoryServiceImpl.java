package com.holoyolo.app.clubSuccessHistory.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.club.service.ClubVO;
import com.holoyolo.app.clubSuccessHistory.mapper.ClubSuccessHistoryMapper;
import com.holoyolo.app.clubSuccessHistory.service.ClubSuccessHistoryService;

@Service
public class ClubSuccessHistoryServiceImpl implements ClubSuccessHistoryService {
	
	@Autowired
	ClubSuccessHistoryMapper clubSuccessHistoryMapper;

}
