package com.holoyolo.app.pointHistory.mapper;

import java.util.List;

import com.holoyolo.app.member.service.MemberVO;
import com.holoyolo.app.pointHistory.service.PointHistoryVO;


public interface PointHistoryMapper {
	//포인트 조회
	public int pointBalance(MemberVO vo);
	
	//포인트내역조회
	public List<PointHistoryVO> getPointHistory(MemberVO vo);

}
