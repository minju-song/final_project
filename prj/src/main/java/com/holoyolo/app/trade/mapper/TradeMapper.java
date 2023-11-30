package com.holoyolo.app.trade.mapper;

import java.util.List;

import com.holoyolo.app.trade.service.TradeVO;

public interface TradeMapper {
	//전체조회
	public List<TradeVO> selectTradeList();
	
	//단건조회
	public TradeVO selectTrade(TradeVO tradeVO);
	
	//등록
	public int insertTrade(TradeVO tradeVO);
	
	//수정
	public int updateTrade(TradeVO tradeVO);
	
	//삭제
	public int deleteTrade(int tradeId);
	
	//중고거래 목록 페이징
	public List<TradeVO> getTradeList(TradeVO tradeVO);
	
	//중고거래 갯수
	public int cntData(TradeVO tradeVO);
	
	//중고거래 목록
	public List<TradeVO> getAllTradeList();
}