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

@Service
public class ClubSuccessHistoryMemberServiceImpl implements ClubSuccessHistoryMemberService {
	
	@Autowired
	ClubSuccessHistoryMemberMapper clubSuccessHistoryMemberMapper;

	@Override
	public Map<String, Object> getFiveHistory(ClubMemberVO vo) {
		Map<String, Object> map = new HashMap<>();
		
		List<ClubSuccessHistoryMemberVO> list = clubSuccessHistoryMemberMapper.getSuccessMember(vo);
		map.put("list", list);
		
		return map;
	}
	
	
}
