package com.holoyolo.app.accBudget.service;

import java.util.HashMap;
import java.util.Map;

public interface AccBudgetService {
	//회원별 현재 예산단위조회
	public Map<String, Object> getBudgetNow(String id);
	
	//예산 등록
	public int insertBudget(AccBudgetVO vo);
	
	//예산단위 사용여부 n으로 바꿈
	public int updateBudget(AccBudgetVO vo);
	
	//예산 아이디
	public AccBudgetVO selectBudid(AccBudgetVO vo);
	
	//예산삭제
	public void deleteBudget(String id);
}
