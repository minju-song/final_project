package com.holoyolo.app.clubMember.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.holoyolo.app.clubMember.service.ClubMemberService;

@Controller
public class ClubMemberController {
	
	@Autowired
	ClubMemberService clubMemberService;
}
