package com.holoyolo.app.holopayHistory.service;

import java.util.List;

import com.holoyolo.app.member.service.MemberVO;

public interface HoloPayHistoryService {

	// 다수조회
	public List<HoloPayHistoryVO> holopayHistoryList();

	// 단건조회
	public HoloPayHistoryVO holopayInfo(HoloPayHistoryVO vo);

	// 등록
	public HoloPayHistoryVO insertHolopayHistory(HoloPayHistoryVO vo);
	
	// 삭제?
	
	//잔액조회
	public int holopayBalance(MemberVO vo);
}
