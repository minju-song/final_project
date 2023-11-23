package com.holoyolo.app.holopayHistory.service;

import java.util.List;

public interface HoloPayHistoryService {

	// 다수조회
	public List<HoloPayHistoryVO> holopayHistoryList();

	// 단건조회
	public HoloPayHistoryVO holopayInfo(HoloPayHistoryVO vo);

	// 등록
	public int insertHolopayHistory(HoloPayHistoryVO vo);
	
	// 삭제?
}
