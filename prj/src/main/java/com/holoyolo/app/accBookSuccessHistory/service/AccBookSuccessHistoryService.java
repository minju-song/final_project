package com.holoyolo.app.accBookSuccessHistory.service;

import com.holoyolo.app.accBudget.service.AccBudgetVO;

public interface AccBookSuccessHistoryService {
	
	//날짜마다 성공여부가져옴
	public String getSuccessByDay(AccBookSuccessHistoryVO vo);
	
}
