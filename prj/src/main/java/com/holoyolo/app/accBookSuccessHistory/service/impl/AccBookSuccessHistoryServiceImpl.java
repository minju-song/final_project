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

	@Override
	public int deleteIng(String id) {
		return accBookSuccessHistoryMapper.deleteIng(id);
	}
}
