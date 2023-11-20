package com.holoyolo.app.accBookHistory.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.accBookHistory.mapper.AccBookHistoryMapper;
import com.holoyolo.app.accBookHistory.service.AccBookHistoryService;

@Service
public class AccBookHistoryServiceImpl implements AccBookHistoryService {
	
	@Autowired
	AccBookHistoryMapper accBookHistoryMapper;
}
