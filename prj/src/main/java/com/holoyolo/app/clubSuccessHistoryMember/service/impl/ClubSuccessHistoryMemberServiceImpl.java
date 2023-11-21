package com.holoyolo.app.clubSuccessHistoryMember.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.clubSuccessHistoryMember.mapper.ClubSuccessHistoryMemberMapper;
import com.holoyolo.app.clubSuccessHistoryMember.service.ClubSuccessHistoryMemberService;

@Service
public class ClubSuccessHistoryMemberServiceImpl implements ClubSuccessHistoryMemberService {
	
	@Autowired
	ClubSuccessHistoryMemberMapper clubSuccessHistoryMemberMapper;
}
