package com.holoyolo.app.clubSuccessHistory.mapper;

import com.holoyolo.app.club.service.ClubVO;
import com.holoyolo.app.clubSuccessHistory.service.ClubSuccessHistoryVO;

public interface ClubSuccessHistoryMapper {

	//해당 클럽의 평균 성공률
	public double getSuccessPct(ClubVO vo);
	
	//해당 클럽의 진행중인 정보
	public ClubSuccessHistoryVO getIng(int id);
}
