package com.holoyolo.app.accBudget.service;

import java.util.Map;

public interface AccBudgetService {
	//회원별 현재 예산단위조회
	public Map<String, Object> getBudgetNow(String id);
	
	//예산 등록
	public int insertBudget(AccBudgetVO vo);
}
