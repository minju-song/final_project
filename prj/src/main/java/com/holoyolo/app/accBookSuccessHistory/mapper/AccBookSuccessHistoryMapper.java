package com.holoyolo.app.accBookSuccessHistory.mapper;

import java.util.HashMap;
import java.util.List;

import com.holoyolo.app.accBookSuccessHistory.service.AccBookSuccessHistoryVO;
import com.holoyolo.app.accBudget.service.AccBudgetVO;

public interface AccBookSuccessHistoryMapper {
	
	//날짜마다 성공여부가져옴
	public AccBookSuccessHistoryVO getSuccessByDay(AccBookSuccessHistoryVO vo);
	
	//예산 처음 등록 시 자동으로 성공여부 체크
	public int insertSuccess(HashMap<String, Object> map);
	
	//예산 수정 시 현재 진행 중인 예산성공기록 삭제
	public int deleteIng(String id);
	
	//날짜마다 성공기록
	public AccBookSuccessHistoryVO getSuccessRecord(AccBookSuccessHistoryVO vo);
}
