package com.holoyolo.app.clubSuccessHistory.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.holoyolo.app.clubSuccessHistory.service.ClubSuccessHistoryService;

@Controller
public class ClubSuccessHistoryController {
	
	@Autowired
	ClubSuccessHistoryService clubSuccessHistoryService;
}
