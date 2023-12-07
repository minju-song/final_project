package com.holoyolo.app.chat.mapper;

import java.util.List;

import com.holoyolo.app.chat.service.ChatRoomVO;
import com.holoyolo.app.chat.service.ChatVO;

public interface ChatRoomMapper {
	
	//채팅방개설
	public int createChatRoom(int clubId);
	
	//채팅방조회
	public ChatRoomVO getChatRoom(int clubId);
	
	//공지사항저장
	public int insertChat(ChatVO vo);
	
	//제일 마지막 공지사항 가져오기
	public String getLatestNotice(int clubId);
	
	//공지사항 리스트
	public List<ChatVO> getNoticeList(int clubId);
}
