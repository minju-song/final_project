package com.holoyolo.app.chat.service.club;

import java.util.HashSet;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ChatRoomVO {
	
	//채팅방아이디는 클럽아이디
    private int clubId;
    
    //현재 참여한 참여자들 저장
    private Set<WebSocketSession> sessions = new HashSet<>();
    @Builder
    public ChatRoomVO(int clubId) {
        this.clubId = clubId;
    }
}
