package com.holoyolo.app.trade.service;

import java.util.List;
import java.util.Map;

import com.holoyolo.app.attachment.service.AttachmentVO;

public interface TradeService {
	
	//단건조회
	public TradeVO getTrade(TradeVO tradeVO);
	
	//등록
	public int insertTrade(TradeVO tradeVO, List<AttachmentVO> imgList);
	
	//수정
	public Map<String, Object> updateTrade(TradeVO tradeVO);
	
	//삭제
	public int deleteTrade(TradeVO tradeVO);
	
	//리스트 페이징
	public Map<String, Object> getTradeList(TradeVO tradeVO);
	
	//조회수 증가
	public int updateViews(TradeVO tradeVO);
}