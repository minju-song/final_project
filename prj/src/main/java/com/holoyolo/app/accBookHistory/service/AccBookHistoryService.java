package com.holoyolo.app.accBookHistory.service;

public interface AccBookHistoryService {
	public AccBookHistoryVO test(AccBookHistoryVO vo);
	
	//api호출 후 저장
	public int insertAccApi(AccBookHistoryVO vo);
}
