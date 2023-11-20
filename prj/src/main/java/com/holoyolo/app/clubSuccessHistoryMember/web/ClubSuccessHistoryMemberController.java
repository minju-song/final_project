package com.holoyolo.app.clubSuccessHistoryMember.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.holoyolo.app.clubSuccessHistoryMember.service.ClubSuccessHistoryMemberService;

@Controller
public class ClubSuccessHistoryMemberController {
	
	@Autowired
	ClubSuccessHistoryMemberService clubSuccessHistoryMemberService;
}
