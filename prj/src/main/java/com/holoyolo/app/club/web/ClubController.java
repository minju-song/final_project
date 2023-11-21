package com.holoyolo.app.club.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.holoyolo.app.club.service.ClubService;

@Controller
public class ClubController {
	
	@Autowired
	ClubService clubService;
}
