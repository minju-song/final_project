package com.holoyolo.app.clubBudget.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.clubBudget.mapper.ClubBudgetMapper;
import com.holoyolo.app.clubBudget.service.ClubBudgetService;

@Service
public class ClubBudgetServiceImpl implements ClubBudgetService {
	
	@Autowired
	ClubBudgetMapper clubBudgetMapper;
}
