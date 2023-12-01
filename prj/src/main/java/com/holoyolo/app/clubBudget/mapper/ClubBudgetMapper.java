package com.holoyolo.app.clubBudget.mapper;

import com.holoyolo.app.clubBudget.service.ClubBudgetVO;

public interface ClubBudgetMapper {

	//클럽의 해당 예산정보
	public ClubBudgetVO getClubBudget(int id);
	
	//클럽예산입력
	public int insertClubBudget(ClubBudgetVO vo);
	
	//클럽예산 사용안함으로 바꿈
	public int updateUseN(ClubBudgetVO vo);
}
