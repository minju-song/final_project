package com.holoyolo.app.accBookHistory.service;


public interface AccBookHistoryService {
	public AccBookHistoryVO test(AccBookHistoryVO vo);
	
	//api호출 후 저장
	public int insertAccApi(AccBookHistoryVO vo);
	
	//db에 들어있는 데이터 중 가장 최근 날짜
	public String getLatestPayDate();
	
}
