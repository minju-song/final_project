package com.holoyolo.app.accBookHistory.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.accBookHistory.mapper.AccBookHistoryMapper;
import com.holoyolo.app.accBookHistory.service.AccBookHistoryService;
import com.holoyolo.app.accBookHistory.service.AccBookHistoryVO;

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
	public int insertAccApi(AccBookHistoryVO vo) {
		return accBookHistoryMapper.insertAccApi(vo);
		
	}

	@Override
	public String getLatestPayDate() {
		AccBookHistoryVO vo = new AccBookHistoryVO();
		
		vo.setMemberId("testminju@mail.com");
		return accBookHistoryMapper.getLatestPayDate(vo);
	}

	

}
