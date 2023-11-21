package com.holoyolo.app.holopayHistory.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.holoyolo.app.holopayHistory.mapper.HoloPayHistoryMapper;
import com.holoyolo.app.holopayHistory.service.HoloPayHistoryService;
import com.holoyolo.app.holopayHistory.service.HoloPayHistoryVO;

@Service
public class HoloPayHistoryServiceImpl implements HoloPayHistoryService {

	@Autowired
	HoloPayHistoryMapper holopayHistoryMapper;

	@Override
	public List<HoloPayHistoryVO> holopayHistoryList() {

		return holopayHistoryMapper.holopayHistoryList();
	}

	@Override
	public HoloPayHistoryVO holopayInfo(HoloPayHistoryVO vo) {

		return holopayHistoryMapper.holopayInfo(vo);
	}

	@Override
	public int insertHolopayHistory(HoloPayHistoryVO vo) {

		return holopayHistoryMapper.insertHolopayHistory(vo);
	}

	@Autowired
	RestTemplate restTemplate;

	
}
