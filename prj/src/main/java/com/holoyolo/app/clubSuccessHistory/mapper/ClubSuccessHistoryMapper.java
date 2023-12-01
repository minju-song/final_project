package com.holoyolo.app.clubSuccessHistory.mapper;

import com.holoyolo.app.club.service.ClubVO;
import com.holoyolo.app.clubBudget.service.ClubBudgetVO;
import com.holoyolo.app.clubSuccessHistory.service.ClubSuccessHistoryVO;

public interface ClubSuccessHistoryMapper {

	//해당 클럽의 평균 성공률
	public double getSuccessPct(ClubVO vo);
	
	//해당 클럽의 진행중인 정보
	public ClubSuccessHistoryVO getIng(int id);
	
	//진행중인 성공기록 넣기
	public int insertSuccessIng(ClubBudgetVO vo);
	
	//현재 진행중인 성공기록 삭제
	public int deleteIng(ClubBudgetVO vo);
}
