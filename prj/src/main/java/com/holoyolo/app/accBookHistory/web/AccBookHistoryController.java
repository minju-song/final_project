package com.holoyolo.app.accBookHistory.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.holoyolo.app.accBookHistory.service.AccBookHistoryService;

@Controller
public class AccBookHistoryController {
	
	@Autowired 
	AccBookHistoryService accBookHistoryService;
}
