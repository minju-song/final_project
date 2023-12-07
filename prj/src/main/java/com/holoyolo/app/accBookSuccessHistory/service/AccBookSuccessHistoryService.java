package com.holoyolo.app.accBookSuccessHistory.service;




public interface AccBookSuccessHistoryService {
	
	//날짜마다 성공여부가져옴
	public String getSuccessByDay(AccBookSuccessHistoryVO vo);
	
	//날짜마다 성공기록
	public AccBookSuccessHistoryVO getSuccessRecord(AccBookSuccessHistoryVO vo);
}
