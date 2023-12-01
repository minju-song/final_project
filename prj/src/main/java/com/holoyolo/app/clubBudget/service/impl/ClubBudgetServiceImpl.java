package com.holoyolo.app.clubBudget.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.clubBudget.mapper.ClubBudgetMapper;
import com.holoyolo.app.clubBudget.service.ClubBudgetService;
import com.holoyolo.app.clubBudget.service.ClubBudgetVO;
import com.holoyolo.app.clubSuccessHistory.mapper.ClubSuccessHistoryMapper;

@Service
public class ClubBudgetServiceImpl implements ClubBudgetService {
	
	@Autowired
	ClubBudgetMapper clubBudgetMapper;
	
	@Autowired
	ClubSuccessHistoryMapper clubSuccessHistoryMapper;

	//클럽예산변경
	@Override
	public String updateClubBudget(ClubBudgetVO vo) {
		//원래 예산 N으로 바꿔줌
		if(clubBudgetMapper.updateUseN(vo) > 0 && clubSuccessHistoryMapper.deleteIng(vo) > 0) {
			//그 뒤 새 예산 넣어줌
			if(clubBudgetMapper.insertClubBudget(vo) > 0 && clubSuccessHistoryMapper.insertSuccessIng(vo) > 0) {
				return "success";
			}
			return "fail";
		}
		return "fail";
	}

}
