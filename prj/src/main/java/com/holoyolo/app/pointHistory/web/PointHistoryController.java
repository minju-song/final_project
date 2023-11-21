package com.holoyolo.app.pointHistory.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.holoyolo.app.pointHistory.service.PointHistoryService;

@Controller
public class PointHistoryController {
	
	@Autowired
	PointHistoryService pointHistoryService;

}
