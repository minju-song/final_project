package com.holoyolo.app.pointHistory.service;

import java.util.Map;


import com.holoyolo.app.member.service.MemberVO;

public interface PointHistoryService {
	//포인트 조회
	public int pointBalance(MemberVO vo);
	
	//마이페이지 포인트내역 페이지 이동
	public Map<String, Object> pageMyPoint(MemberVO vo);

}
