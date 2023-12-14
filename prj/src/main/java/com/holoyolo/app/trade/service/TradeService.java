package com.holoyolo.app.trade.service;

import java.util.List;
import java.util.Map;

import com.holoyolo.app.attachment.service.AttachmentVO;
import com.holoyolo.app.member.service.MemberVO;

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
	
	//구매자 수정
	public int updateBuyerId(TradeVO tradeVO);

	//중고거래 이미지 수정(insert)
	public int updateTradeImg(TradeVO tradeVO, List<AttachmentVO> imgList);
	
	//포인트, 홀로페이 등록(구매자)
	public Map<String, Object> insertPayPoint(MemberVO memberVO);
	
	//포인트, 홀로페이 등록(판매자)
		public Map<String, Object> insertPayPointSeller(MemberVO memberVO);
	
	//마이페이지 전체조회
	public List<TradeVO> selectMyTradeList(TradeVO tradeVO);
	
	//
	public TradeVO selectTrade2(TradeVO vo);
}