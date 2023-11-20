package com.holoyolo.app.club.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.club.mapper.ClubMapper;
import com.holoyolo.app.club.service.ClubService;

@Service
public class ClubServiceImpl implements ClubService {
	
	@Autowired
	ClubMapper clubMapper;
}
