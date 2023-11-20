package com.holoyolo.app.clubMember.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.clubMember.mapper.ClubMemberMapper;
import com.holoyolo.app.clubMember.service.ClubMemberService;

@Service
public class ClubMemberServiceImpl implements ClubMemberService {
	
	@Autowired
	ClubMemberMapper clubMemberMapper;
}
