package com.holoyolo.app.holopayHistory.mapper;

import java.util.List;
import java.util.Optional;

import com.holoyolo.app.holopayHistory.service.HoloPayHistoryVO;
import com.holoyolo.app.member.service.MemberVO;

public interface HoloPayHistoryMapper {

	public List<HoloPayHistoryVO> totalHolopayHistoryList();

	// 다수조회
	public List<HoloPayHistoryVO> holopayHistoryList(HoloPayHistoryVO vo);

	// 단건조회
	public HoloPayHistoryVO holopayInfo(HoloPayHistoryVO vo);

	// 등록
	public HoloPayHistoryVO insertHolopayHistory(HoloPayHistoryVO vo);

	// 잔액조회
	public int holopayBalance(MemberVO vo);

	// 기간별 조회
	public List<HoloPayHistoryVO> searchPay(HoloPayHistoryVO vo);

	List<HoloPayHistoryVO> searchPayPaged(String memberId, int searchMonth, int start, int end);

	int getTotalRecords(String memberId, int searchMonth);
}
