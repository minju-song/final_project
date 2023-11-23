package com.holoyolo.app.accBudget.mapper;

import com.holoyolo.app.accBudget.service.AccBudgetVO;

public interface AccBudgetMapper {
	
	//회원별 현재 예산단위조회
	public AccBudgetVO getBudgetNow(AccBudgetVO vo);
	
	//예산단위 등록
	public int insertBudget(AccBudgetVO vo);
	
	//예산단위 사용여부 n으로 바꿈
	public int updateBudget(AccBudgetVO vo);
}
