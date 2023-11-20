package com.holoyolo.app.clubBudget.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.holoyolo.app.clubBudget.service.ClubBudgetService;

@Controller
public class ClubBudgetController {
	
	@Autowired
	ClubBudgetService clubBudgetService;
}
