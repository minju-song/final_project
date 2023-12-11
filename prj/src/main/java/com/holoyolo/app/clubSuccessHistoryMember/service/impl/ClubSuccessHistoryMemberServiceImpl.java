package com.holoyolo.app.clubSuccessHistoryMember.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.clubMember.service.ClubMemberVO;
import com.holoyolo.app.clubSuccessHistoryMember.mapper.ClubSuccessHistoryMemberMapper;
import com.holoyolo.app.clubSuccessHistoryMember.service.ClubSuccessHistoryMemberService;
import com.holoyolo.app.clubSuccessHistoryMember.service.ClubSuccessHistoryMemberVO;
import com.holoyolo.app.member.mapper.MemberMapper;
import com.holoyolo.app.member.service.MemberVO;

@Service
public class ClubSuccessHistoryMemberServiceImpl implements ClubSuccessHistoryMemberService {
	
	@Autowired
	ClubSuccessHistoryMemberMapper clubSuccessHistoryMemberMapper;

	@Autowired
	MemberMapper memberMapper;
	
	@Override
	public Map<String, Object> getFiveHistory(ClubMemberVO vo) {
		Map<String, Object> map = new HashMap<>();
		
		List<ClubSuccessHistoryMemberVO> list = clubSuccessHistoryMemberMapper.getSuccessMember(vo);
		map.put("list", list);
		
		MemberVO mvo = memberMapper.selectUser(vo.getMemberId());
		map.put("member", mvo);
		
		return map;
	}
	
	
}
