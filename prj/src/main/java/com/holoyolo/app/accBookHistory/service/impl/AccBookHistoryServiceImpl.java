package com.holoyolo.app.accBookHistory.service.impl;




import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

	@Override
	public int insertAcc(AccBookHistoryVO vo) {
		return accBookHistoryMapper.insertAcc(vo);
		
	}

	@Override
	public String getLatestPayDate(String id) {
		AccBookHistoryVO vo = new AccBookHistoryVO();
		
		vo.setMemberId(id);
		return accBookHistoryMapper.getLatestPayDate(vo);
	}

	@Override
	public List<AccBookHistoryVO> getAccHistory(AccBookHistoryVO vo) {

		

		
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd");             
//        LocalDateTime date = LocalDateTime.parse(vo.getPayDate(), formatter);

		vo.setPayDate(vo.getPayDate());
		
		List<AccBookHistoryVO> list = accBookHistoryMapper.getAccHistory(vo);
		
		

		return list;
	}

	@Override
	public int getSumPrice(AccBookHistoryVO vo) {
		
		int price = accBookHistoryMapper.getSumPrice(vo);

		return price;
	}

	@Override
	public int getMonthPrice(AccBookHistoryVO vo) {
		int price = accBookHistoryMapper.getMonthPrice(vo);
		
		return price;
	}

	

}
