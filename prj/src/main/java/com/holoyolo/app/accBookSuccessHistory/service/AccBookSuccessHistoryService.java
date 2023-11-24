package com.holoyolo.app.accBookSuccessHistory.service;

import com.holoyolo.app.accBudget.service.AccBudgetVO;

public interface AccBookSuccessHistoryService {
	
	//날짜마다 성공여부가져옴
	public String getSuccessByDay(AccBookSuccessHistoryVO vo);
	
	//예산 처음 등록 시 자동으로 성공여부 체크
	public boolean insertSuccess(AccBudgetVO vo);
	
	//예산 수정 시 현재 진행 중인 예산성공기록 삭제
	public int deleteIng(String id);
}
