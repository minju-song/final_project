package com.holoyolo.app.trade.service;

import java.util.List;
import java.util.Map;

public interface TradeService {
	//전체조회
	public List<TradeVO> getTradeList();
	
	//단건조회
	public TradeVO getTrade(TradeVO tradeVO);
	
	//등록
	public int insertTrade(TradeVO tradeVO);
	
	//수정
	public Map<String, Object> updateTrade(TradeVO tradeVO);
	
	//삭제
	public int deleteTrade(int tradeId);
	
	//리스트 페이징
	public Map<String, Object> tradePaging(TradeVO tradeVO);
}