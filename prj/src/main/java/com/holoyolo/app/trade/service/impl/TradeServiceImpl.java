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
	
	@Override
	public List<TradeVO> getTradeList() {
		return tradeMapper.selectTradeList();
	}

	@Override
	public TradeVO getTrade(TradeVO tradeVO) {
		return tradeMapper.selectTrade(tradeVO);
	}

	@Override
	public int insertTrade(TradeVO tradeVO) {
		int result = tradeMapper.insertTrade(tradeVO);
		
		if(result == 1) {
			return tradeVO.getTradeId();
		}
		return -1;
	}

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

	@Override
	public int deleteTrade(int tradeId) {
		return tradeMapper.deleteTrade(tradeId);
	}

}