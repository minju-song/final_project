package com.holoyolo.app.holopayHistory.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.holoyolo.app.holopayHistory.mapper.HoloPayHistoryMapper;
import com.holoyolo.app.holopayHistory.service.HoloPayHistoryService;
import com.holoyolo.app.holopayHistory.service.HoloPayHistoryVO;
import com.holoyolo.app.member.service.MemberVO;

@Service
public class HoloPayHistoryServiceImpl implements HoloPayHistoryService {

	@Autowired
	HoloPayHistoryMapper holopayHistoryMapper;

	@Override
	public List<HoloPayHistoryVO> holopayHistoryList(HoloPayHistoryVO vo) {

		return holopayHistoryMapper.holopayHistoryList(vo);
	}

	@Override
	public HoloPayHistoryVO holopayInfo(HoloPayHistoryVO vo) {

		return holopayHistoryMapper.holopayInfo(vo);
	}

	@Override
	public HoloPayHistoryVO insertHolopayHistory(HoloPayHistoryVO vo) {

		return holopayHistoryMapper.insertHolopayHistory(vo);
	}

	@Autowired
	RestTemplate restTemplate;

	@Override
	public int holopayBalance(MemberVO vo) {

		return holopayHistoryMapper.holopayBalance(vo);
	}

	@Override
	public List<HoloPayHistoryVO> totalHolopayHistoryList() {

		return holopayHistoryMapper.totalHolopayHistoryList();
	}

	@Override
	public List<HoloPayHistoryVO> searchPay(String str, HoloPayHistoryVO vo) {
		List<HoloPayHistoryVO> rtn = new ArrayList<HoloPayHistoryVO>();
		switch (str) {
		case "all":
				rtn =holopayHistoryList(vo);
			break;
		case "1M":
			vo.setSearchMonth(-1);
			rtn = holopayHistoryMapper.searchPay(vo);
			break;
		case "3M":
			vo.setSearchMonth(-3);
			rtn = holopayHistoryMapper.searchPay(vo);
			break;
		case "6M":
			vo.setSearchMonth(-6);
			rtn = holopayHistoryMapper.searchPay(vo);
			break;
		default:
			break;
		}
		return rtn;
	}

}
