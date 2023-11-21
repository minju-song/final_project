package com.holoyolo.app.pointHistory.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.pointHistory.mapper.PointHistoryMapper;
import com.holoyolo.app.pointHistory.service.PointHistoryService;

@Service
public class PointHistoryServiceImpl implements PointHistoryService {
	
	@Autowired
	PointHistoryMapper pointHistoryMapper;

}
