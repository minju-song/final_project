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

	//현재 회원의 예산얻기
	@Override
	public Map<String, Object> getBudgetNow(String id) {
		Map<String,Object> map = new HashMap<String, Object>();
		AccBudgetVO vo = new AccBudgetVO();
		vo.setMemberId(id);
		
		//해당 회원의 예산가져옴
		vo = accBudgetMapper.getBudgetNow(vo);
		System.out.println(vo);
		
		//예산 데이터가 있으면
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

	//예산등록
	@Override
	public int insertBudget(AccBudgetVO vo) {
		return accBudgetMapper.insertBudget(vo);
	}

	//예산수정
	@Override
	public int updateBudget(AccBudgetVO vo) {
		return accBudgetMapper.updateBudget(vo);
	}

	//예산아이디가져오기
	@Override
	public AccBudgetVO selectBudid(AccBudgetVO vo) {
		return accBudgetMapper.getBudgetNow(vo);
	}

	//예산삭제
	@Override
	public void deleteBudget(String id) {
		accBudgetMapper.deleteBudget(id);
	}
}
