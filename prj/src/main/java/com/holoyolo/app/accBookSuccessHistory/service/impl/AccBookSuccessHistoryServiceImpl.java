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

}
