package com.holoyolo.app.holopayHistory.mapper;

import java.util.List;

import com.holoyolo.app.holopayHistory.service.HoloPayHistoryVO;

public interface HoloPayHistoryMapper {

		// 다수조회
		public List<HoloPayHistoryVO> holopayHistoryList();

		// 단건조회
		public HoloPayHistoryVO holopayInfo(HoloPayHistoryVO vo);

		// 등록
		public int insertHolopayHistory(HoloPayHistoryVO vo);
}
