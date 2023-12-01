package com.holoyolo.app.accBudget.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.accBookSuccessHistory.mapper.AccBookSuccessHistoryMapper;
import com.holoyolo.app.accBudget.mapper.AccBudgetMapper;
import com.holoyolo.app.accBudget.service.AccBudgetService;
import com.holoyolo.app.accBudget.service.AccBudgetVO;

@Service
public class AccBudgetServiceImpl implements AccBudgetService {
	
	@Autowired
	AccBudgetMapper accBudgetMapper;

	@Autowired
	AccBookSuccessHistoryMapper accBookSuccessHistoryMapper;
	
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
	public boolean insertBudget(AccBudgetVO vo) {
		int ck = accBudgetMapper.insertBudget(vo);
		if(ck < 1) {
			System.out.println("입력안됨");
			
		}
		else {
			HashMap<String, Object> map = new HashMap<>();
			map.put("memberId", vo.getMemberId());
			if (accBookSuccessHistoryMapper.insertSuccess(map) > 0) {
				return true;
			}
			else {
				return false;
			}
		}
		
		return false;
		
	}

	//예산수정
	@Override
	public int updateBudget(AccBudgetVO vo) {
		int ck = accBudgetMapper.updateBudget(vo);
		//이전 예산데이터 사용여부 N으로 수정
		if(ck > 0) {			
			ck = accBudgetMapper.insertBudget(vo);
			//예산 데이터 입력
			if(ck < 1) {
				System.out.println("입력안됨");
			}
			else {
				HashMap<String, Object> map = new HashMap<>();
				map.put("memberId", vo.getMemberId());
				accBookSuccessHistoryMapper.deleteIng(vo.getMemberId());
				accBookSuccessHistoryMapper.insertSuccess(map);
			}
		}
		return ck;
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
