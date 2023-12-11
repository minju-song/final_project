package com.holoyolo.app.chat.service.trade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.holoyolo.app.chat.mapper.TradeChatRoomMapper;
import com.holoyolo.app.member.mapper.MemberMapper;
import com.holoyolo.app.member.service.MemberVO;
import com.holoyolo.app.trade.service.TradeVO;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;

@Slf4j
@RequiredArgsConstructor
@Service
public class TradeChatService {
	
	private final TradeChatRoomMapper tradeChatRoomMapper;
	
	@Autowired
	MemberMapper memberMapper;
	
	 private Map<Integer, TradeChatRoomVO> tradeChatRooms = new LinkedHashMap<>();
	 
	    //채팅방찾기
	    public TradeChatRoomVO findClubById(TradeVO trade) {
	        return tradeChatRooms.get(trade.getTradeId());
	    }
	    
	    //채팅방개설
	    public TradeChatRoomVO createRoom(TradeVO trade) {
	    	TradeChatRoomVO chatRoom = TradeChatRoomVO.builder()
	                .tradeId(trade.getTradeId())
	                .title(trade.getTitle())
	                .sellerId(trade.getSellerId())
	                .buyerId(trade.getBuyerId())
	                .build();
	    	tradeChatRoomMapper.createTradeChatRoom(trade); // 데이터베이스에 방 생성
	        return chatRoom;
	    }
	    
	    //채팅방만들거나 찾기
	    public TradeChatRoomVO findOrCreateRoom(TradeVO trade) {
	    	System.out.println(trade);
	    	TradeChatRoomVO chatRoom = tradeChatRooms.get(trade.getTradeId());
	    	
	        if (chatRoom == null) {
	            chatRoom = tradeChatRoomMapper.getTradeChatRoom(trade.getTradeId()); // 데이터베이스에서 방 찾아오기
	            System.out.println("cr : "+chatRoom);
	            if (chatRoom == null) {
	                // 데이터베이스에도 없으면 새로운 방 생성
	                chatRoom = createRoom(trade);
	                System.out.println("방 생성");
	            } else {
	                System.out.println("방 찾음");
	            }

	            tradeChatRooms.put(trade.getTradeId(), chatRoom); // 메모리에 방 정보 저장
	        }

	        return chatRoom;
	    }
	    
	    //채팅저장
	    public int insertChat(String str) {
	    	ObjectMapper objectMapper = new ObjectMapper();
	    	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy. MM. dd. a H:mm:ss");
	    	TradeChatVO chatObj = null;
			try {
				chatObj = objectMapper.readValue(str, TradeChatVO.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			try {
				chatObj.setRealDate(sdf1.parse(chatObj.getDate()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			chatObj.setTradeChatRoomId(chatObj.getTradeId());
			return tradeChatRoomMapper.insertTradeChat(chatObj);
	    }
	    
	    
	    //내 채팅목록페이지 이동
		public Map<String, Object>getMyChattings(String id) {
			Map<String, Object> map = new HashMap<>();
			
			List<TradeChatRoomVO> list = tradeChatRoomMapper.getMyChattings(id);
			for(int i = 0; i < list.size(); i++) {
				if (list.get(i).getBuyerId().equals(id)) {
					MemberVO you = memberMapper.selectUser(list.get(i).getSellerId());
					list.get(i).setYou(you);
				}
				else {
					MemberVO you = memberMapper.selectUser(list.get(i).getBuyerId());
					list.get(i).setYou(you);
				}
			}
			
			System.out.println(list);
			
			
			map.put("list", list);
			return map;
		}
		
		//해당 채팅방의 채팅들
		public List<TradeChatVO> getChat(int id) {
			return tradeChatRoomMapper.getChat(id);
		}
		
		//채팅방입장 시 모두 읽음
		public int updateAllChat(TradeChatVO vo) {
			return tradeChatRoomMapper.updateAllChat(vo);
		}
		
		//실시간 채팅 읽음 
		public int updateRead(TradeChatVO vo) {
			return tradeChatRoomMapper.updateRead(vo);
		}
		
		//헤더에 올릴 메시지
		public Map<String, Object> checkNew(String id) {
			Map<String, Object> map = new HashMap<>();
			
			if(tradeChatRoomMapper.selectNotreadCount(id) > 0) map.put("result", true);
			else map.put("result", false);
			return map;
		}
}
