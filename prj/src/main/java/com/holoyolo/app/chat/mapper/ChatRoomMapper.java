package com.holoyolo.app.chat.mapper;

import com.holoyolo.app.chat.service.ChatRoomVO;
import com.holoyolo.app.chat.service.ChatVO;

public interface ChatRoomMapper {
	
	//채팅방개설
	public int createChatRoom(int clubId);
	
	//채팅방조회
	public ChatRoomVO getChatRoom(int clubId);
	
	//채팅저장
	public int insertChat(ChatVO vo);
}
