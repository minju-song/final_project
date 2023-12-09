package com.holoyolo.app.chat.mapper;

import java.util.List;

import com.holoyolo.app.chat.service.trade.TradeChatRoomVO;
import com.holoyolo.app.chat.service.trade.TradeChatVO;
import com.holoyolo.app.trade.service.TradeVO;

public interface TradeChatRoomMapper {

	//채팅방 조회
	public TradeChatRoomVO getTradeChatRoom(int tradeId);
	
	//채팅방 개설
	public int createTradeChatRoom(TradeVO vo);
	
	//채팅 저장
	public int insertTradeChat(TradeChatVO vo);
	
	//나의 채팅목록
	public List<TradeChatRoomVO> getMyChattings(String id);
	
	//해당 채팅방의 채팅들
	public List<TradeChatVO> getChat(int id);
	
	//채팅방입장 시 모두 읽음
	public int updateAllChat(TradeChatVO vo);
	
	//실시간 채팅 읽음 
	public int updateRead(TradeChatVO vo);
}
