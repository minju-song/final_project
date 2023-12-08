package com.holoyolo.app.pointHistory.mapper;

import com.holoyolo.app.member.service.MemberVO;

public interface PointHistoryMapper {
	//포인트 조회
	public int pointBalance(MemberVO vo);
}
