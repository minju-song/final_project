package com.holoyolo.app.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.member.mapper.MemberMapper;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberMapper memberMapper;
	
	@Override
	public int joinUser(MemberVO memberVO) {
		return memberMapper.joinUser(memberVO);
	}

	@Override
	public MemberVO selectUser(String username) {
		return memberMapper.selectUser(username);
	}
}
