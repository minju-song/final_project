package com.holoyolo.app.accBookSuccessHistory.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.accBookSuccessHistory.mapper.AccBookSuccessHistoryMapper;
import com.holoyolo.app.accBookSuccessHistory.service.AccBookSuccessHistoryService;
import com.holoyolo.app.accBookSuccessHistory.service.AccBookSuccessHistoryVO;
import com.holoyolo.app.accBudget.service.AccBudgetVO;

@Service
public class AccBookSuccessHistoryServiceImpl implements AccBookSuccessHistoryService {
	
	@Autowired
	AccBookSuccessHistoryMapper accBookSuccessHistoryMapper;

	//날마다 성공여부 가져오기
	@Override
	public String getSuccessByDay(AccBookSuccessHistoryVO vo) {
		AccBookSuccessHistoryVO result = new AccBookSuccessHistoryVO();
		
		result = accBookSuccessHistoryMapper.getSuccessByDay(vo);
		if(result != null && !result.getSuccessState().equals("")) {
			return result.getSuccessState();
		}
		else {
			return "null";
		}
	}

	//예산등록 시 성공자동등록
	@Override
	public boolean insertSuccess(AccBudgetVO vo) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("memberId", vo.getMemberId());
		if (accBookSuccessHistoryMapper.insertSuccess(map) > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	//예산변경 시 현재 진행중인 성공기록 삭제
	@Override
	public int deleteIng(String id) {
		return accBookSuccessHistoryMapper.deleteIng(id);
	}
}
