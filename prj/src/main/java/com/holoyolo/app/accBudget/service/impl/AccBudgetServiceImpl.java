package com.holoyolo.app.accBudget.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.accBudget.mapper.AccBudgetMapper;
import com.holoyolo.app.accBudget.service.AccBudgetService;

@Service
public class AccBudgetServiceImpl implements AccBudgetService {
	
	@Autowired
	AccBudgetMapper accBudgetMapper;
}
