package com.holoyolo.app.pointHistory.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.member.service.MemberVO;
import com.holoyolo.app.pointHistory.mapper.PointHistoryMapper;
import com.holoyolo.app.pointHistory.service.PointHistoryService;
import com.holoyolo.app.pointHistory.service.PointHistoryVO;

@Service
public class PointHistoryServiceImpl implements PointHistoryService {
	
	@Autowired
	PointHistoryMapper pointHistoryMapper;

	@Override
	public int pointBalance(MemberVO vo) {
		return pointHistoryMapper.pointBalance(vo);
	}



	//마이페이지 
	@Override
	public Map<String, Object> pageMyPoint(MemberVO vo) {
		Map<String, Object> map = new HashMap<>();
		
		//현재 포인트 잔액
		int nowPoint = pointHistoryMapper.pointBalance(vo);
		
		//포인트 내역
		List<PointHistoryVO> list = pointHistoryMapper.getPointHistory(vo);
		
		map.put("nowPoint", nowPoint);
		map.put("history", list);
		return map;
	}
}
