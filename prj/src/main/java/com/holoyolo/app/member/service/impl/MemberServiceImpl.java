package com.holoyolo.app.member.service.impl;

import java.util.Date;

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

	@Override
	public Date selectJoinDate() {
		MemberVO vo = new MemberVO();
		//회원아이디가져오기
		vo.setMemberId("testminju@mail.com");
		vo = memberMapper.selectJoinDate(vo);
		return vo.getJoinDate();
	}

	@Override
	public MemberVO checkMemberId(MemberVO memberVO) {
		return memberMapper.checkMemberId(memberVO);
	}

	@Override
	public MemberVO checkNickname(MemberVO memberVO) {
		return memberMapper.checkNickname(memberVO);
	}
	
}
