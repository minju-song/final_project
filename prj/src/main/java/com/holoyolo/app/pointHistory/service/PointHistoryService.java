package com.holoyolo.app.pointHistory.service;

import com.holoyolo.app.member.service.MemberVO;

public interface PointHistoryService {
	//포인트 조회
	public int pointBalance(MemberVO vo);
}
