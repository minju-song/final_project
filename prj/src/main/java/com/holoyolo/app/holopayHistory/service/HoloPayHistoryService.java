package com.holoyolo.app.holopayHistory.service;

import java.util.List;

import com.holoyolo.app.member.service.MemberVO;

public interface HoloPayHistoryService {
	//전체조회 (관리자)
	public List<HoloPayHistoryVO> totalHolopayHistoryList();
	// 회원 홀로페이 조회
	public List<HoloPayHistoryVO> holopayHistoryList(HoloPayHistoryVO vo);

	// 단건조회
	public HoloPayHistoryVO holopayInfo(HoloPayHistoryVO vo);

	// 등록
	public HoloPayHistoryVO insertHolopayHistory(HoloPayHistoryVO vo);
	
	// 삭제?
	
	//잔액조회
	public int holopayBalance(MemberVO vo);
	
	//기간별 조회
	public List<HoloPayHistoryVO> searchPay(String str, HoloPayHistoryVO vo);
}
