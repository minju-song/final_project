package com.holoyolo.app.chat.service.club;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.holoyolo.app.chat.mapper.ChatRoomMapper;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {


    private final ChatRoomMapper chatRoomMapper;

    private Map<Integer, ChatRoomVO> chatRooms = new LinkedHashMap<>();
    
    //채팅방찾기
    public ChatRoomVO findClubById(int clubId) {
        return chatRooms.get(clubId);
    }

    //채팅방개설
    public ChatRoomVO createRoom(int clubId) {
        ChatRoomVO chatRoom = ChatRoomVO.builder()
                .clubId(clubId)
                .build();
        chatRoomMapper.createChatRoom(clubId); // 데이터베이스에 방 생성
        return chatRoom;
    }

    //채팅방만들거나 찾기
    public ChatRoomVO findOrCreateRoom(int clubId) {
        ChatRoomVO chatRoom = chatRooms.get(clubId);

        if (chatRoom == null) {
            chatRoom = chatRoomMapper.getChatRoom(clubId); // 데이터베이스에서 방 찾아오기

            if (chatRoom == null) {
                // 데이터베이스에도 없으면 새로운 방 생성
                chatRoom = createRoom(clubId);
                System.out.println("방 생성");
            } else {
                System.out.println("방 찾음");
            }

            chatRooms.put(clubId, chatRoom); // 메모리에 방 정보 저장
        }

        return chatRoom;
    }
    
    //공지사항 저장
    public int insertChat(String chat) {
    	System.out.println("들어온 메시지 : "+chat);
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy. MM. dd. a KK:mm:ss", new Locale("ko", "KR"));
    	
    	//json -> 객체
    	ChatVO chatObj = null;
		try {
			chatObj = objectMapper.readValue(chat, ChatVO.class);
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
    	System.out.println(chatObj);
    	
    	return chatRoomMapper.insertChat(chatObj);
    }
    
	//제일 마지막 공지사항 가져오기
	public String getLatestNotice(int clubId) {
		
		return chatRoomMapper.getLatestNotice(clubId);
	};
	
	//공지사항 리스트 가져오기
	public Map<String, Object> getNoticeList(int clubId) {
		Map<String, Object> map = new HashMap<>();
		List<ChatVO> list = chatRoomMapper.getNoticeList(clubId);
		
		map.put("list", list);
		
		return map;
	}
	

}
