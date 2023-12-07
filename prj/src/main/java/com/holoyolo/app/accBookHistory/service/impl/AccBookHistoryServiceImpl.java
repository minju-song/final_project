package com.holoyolo.app.accBookHistory.service.impl;




import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import com.holoyolo.app.accBookHistory.mapper.AccBookHistoryMapper;
import com.holoyolo.app.accBookHistory.service.AccBookHistoryService;
import com.holoyolo.app.accBookHistory.service.AccBookHistoryVO;
import com.holoyolo.app.auth.PrincipalDetails;

@Service
public class AccBookHistoryServiceImpl implements AccBookHistoryService {

	@Autowired
	AccBookHistoryMapper accBookHistoryMapper;
	
	@Override
	public AccBookHistoryVO test(AccBookHistoryVO vo) {
		AccBookHistoryVO test = new AccBookHistoryVO();
		test = accBookHistoryMapper.test(vo);
		
		return test;
	}

	//거래내역 수기등록
	@Override
	public int insertAcc(AccBookHistoryVO vo) {
		//거래내역 등록
		if(accBookHistoryMapper.insertAcc(vo)> 0) {
			//방금 등록한 거래내역 아이디 리턴
			return vo.getAbHistoryId();
		}
		return -1;
		
	}

	//가장 최근 거래내역 날짜
	@Override
	public String getLatestPayDate(String id) {
		AccBookHistoryVO vo = new AccBookHistoryVO();
		
		vo.setMemberId(id);
		return accBookHistoryMapper.getLatestPayDate(vo);
	}

	//거래내역 불러오기
	@Override
	public List<AccBookHistoryVO> getAccHistory(AccBookHistoryVO vo) {
		
		List<AccBookHistoryVO> list = accBookHistoryMapper.getAccHistory(vo);
		
		return list;
	}

	//현재 총 소비금액(당일)
	@Override
	public int getSumPrice(AccBookHistoryVO vo) {
		
		int price = accBookHistoryMapper.getSumPrice(vo);

		return price;
	}

	//현재 월 총 소비금액
	@Override
	public int getMonthPrice(AccBookHistoryVO vo) {
		int price = accBookHistoryMapper.getMonthPrice(vo);
		
		return price;
	}

	//거래내역삭제
	@Override
	public int deleteHistory(AccBookHistoryVO vo) {
		return accBookHistoryMapper.deleteHistory(vo);
	}

	@Override
	public int getSumInputPrice(AccBookHistoryVO vo) {

		return accBookHistoryMapper.getSumInputPrice(vo);
	}

	

}
