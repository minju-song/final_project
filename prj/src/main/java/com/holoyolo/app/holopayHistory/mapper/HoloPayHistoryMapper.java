package com.holoyolo.app.holopayHistory.mapper;

import java.util.List;

import com.holoyolo.app.holopayHistory.service.HoloPayHistoryVO;
import com.holoyolo.app.member.service.MemberVO;

public interface HoloPayHistoryMapper {

		// 다수조회
		public List<HoloPayHistoryVO> holopayHistoryList();

		// 단건조회
		public HoloPayHistoryVO holopayInfo(HoloPayHistoryVO vo);

		// 등록
		public HoloPayHistoryVO insertHolopayHistory(HoloPayHistoryVO vo);
		
		//잔액조회
		public int holopayBalance(MemberVO vo);
}
