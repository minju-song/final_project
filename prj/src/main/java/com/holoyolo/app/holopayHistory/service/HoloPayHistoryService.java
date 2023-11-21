package com.holoyolo.app.holopayHistory.service;

import java.util.List;

public interface HoloPayHistoryService {

	// 다수조회
	public List<HoloPayHistoryVO> holopayHistoryList();

	// 단건조회
	public HoloPayHistoryVO holopayInfo();

	// 등록
	public int insertHolopayHistory();
	
	// 삭제?
}
