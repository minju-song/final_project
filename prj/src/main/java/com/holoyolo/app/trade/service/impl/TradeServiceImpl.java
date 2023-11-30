package com.holoyolo.app.trade.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.trade.mapper.TradeMapper;
import com.holoyolo.app.trade.service.TradeService;
import com.holoyolo.app.trade.service.TradeVO;

@Service
public class TradeServiceImpl implements TradeService {

	@Autowired
	TradeMapper tradeMapper;
	
	// 거래 전체조회
	@Override
	public List<TradeVO> getTradeList() {
		return tradeMapper.selectTradeList();
	}
	
	// 거래 단건조회
	@Override
	public TradeVO getTrade(TradeVO tradeVO) {
		return tradeMapper.selectTrade(tradeVO);
	}

	// 거래 등록
	@Override
	public int insertTrade(TradeVO tradeVO) {
		int result = tradeMapper.insertTrade(tradeVO);
		
		if(result == 1) {
			return tradeVO.getTradeId();
		}
		return -1;
	}

	// 거래 수정
	@Override
	public Map<String, Object> updateTrade(TradeVO tradeVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;
		
		int result = tradeMapper.updateTrade(tradeVO);
		if(result == 1) {
			isSuccessed = true;
		}
		
		map.put("result", isSuccessed);
		map.put("info", tradeVO);
		
		return map;
	}

	// 거래 삭제
	@Override
	public int deleteTrade(int tradeId) {
		return tradeMapper.deleteTrade(tradeId);
	}

	//데이터 갯수
	@Override
	public int cntData(TradeVO tradeVO) {
		return tradeMapper.cntData(tradeVO);
	}

	//리스트 페이징
	@Override
	public Map<String, Object> tradePaging(TradeVO tradeVO) {
		Map<String, Object> map = new HashMap<>();
		List<TradeVO> list = tradeMapper.getTradeList(tradeVO);
		
		for(int i=0; i<list.size(); i++) {
			TradeVO temp = new TradeVO();
			temp = list.get(i);
			list.set(i, temp);
		}
		
		map.put("length", list.size());
		map.put("result", list);
		
		return map;
	}

	//리스트 목록
	@Override
	public Map<String, Object> tradeListPage() {
		Map<String, Object> map = new HashMap<>();
		
		List<TradeVO> list = tradeMapper.selectTradeList();
		map.put("list", list);
		
		return map;
	}

}