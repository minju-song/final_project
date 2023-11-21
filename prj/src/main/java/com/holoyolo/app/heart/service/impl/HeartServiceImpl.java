package com.holoyolo.app.heart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.heart.mapper.HeartMapper;
import com.holoyolo.app.heart.service.HeartService;

@Service
public class HeartServiceImpl implements HeartService {
	
	@Autowired
	HeartMapper heartMapper;
	
	
}
