package com.holoyolo.app.accBookSuccessHistory.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.accBookSuccessHistory.mapper.AccBookSuccessHistoryMapper;
import com.holoyolo.app.accBookSuccessHistory.service.AccBookSuccessHistoryService;

@Service
public class AccBookSuccessHistoryServiceImpl implements AccBookSuccessHistoryService {
	
	@Autowired
	AccBookSuccessHistoryMapper accBookSuccessHistoryMapper;
}
