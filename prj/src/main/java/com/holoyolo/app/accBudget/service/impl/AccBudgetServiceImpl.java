package com.holoyolo.app.accBudget.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.accBudget.mapper.AccBudgetMapper;
import com.holoyolo.app.accBudget.service.AccBudgetService;
import com.holoyolo.app.accBudget.service.AccBudgetVO;

@Service
public class AccBudgetServiceImpl implements AccBudgetService {
	
	@Autowired
	AccBudgetMapper accBudgetMapper;

	@Override
	public Map<String, Object> getBudgetNow(String id) {
		Map<String,Object> map = new HashMap<String, Object>();
		AccBudgetVO vo = new AccBudgetVO();
		vo.setMemberId(id);
		vo = accBudgetMapper.getBudgetNow(vo);
		if(vo != null && !vo.getAccBudgetUnit().equals("")) {	
			map.put("예산단위", vo.getAccBudgetUnit());
			map.put("예산금액", vo.getAccBudgetPrice());
		}
		else {
			map.put("예산단위", "null");
			map.put("예산금액", "null");
		}
		return map;
	}

	@Override
	public int insertBudget(AccBudgetVO vo) {
		int i = accBudgetMapper.insertBudget(vo);
		
		
		return i;
	}
}
