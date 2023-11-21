package com.holoyolo.app.accBudget.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.holoyolo.app.accBudget.service.AccBudgetService;

@Controller
public class AccBudgetController {
	
	@Autowired
	AccBudgetService accBudgetService;
}
