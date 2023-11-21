package com.holoyolo.app.accBookSuccessHistory.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.holoyolo.app.accBookSuccessHistory.service.AccBookSuccessHistoryService;

@Controller
public class AccBookSuccessHistoryController {
	
	@Autowired
	AccBookSuccessHistoryService accBookSuccessHistoryService;
}
