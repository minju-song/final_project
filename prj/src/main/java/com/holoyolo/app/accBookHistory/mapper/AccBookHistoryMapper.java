package com.holoyolo.app.accBookHistory.mapper;

import com.holoyolo.app.accBookHistory.service.AccBookHistoryVO;

public interface AccBookHistoryMapper {
	public AccBookHistoryVO test(AccBookHistoryVO vo);
	
	//api호출 후 저장
	public int insertAccApi(AccBookHistoryVO vo);
}
