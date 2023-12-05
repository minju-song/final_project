package com.holoyolo.app.chat.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import lombok.Builder;
import lombok.Data;

@Data
public class ChatRoom {
    private String clubId;
    private Set<WebSocketSession> sessions = new HashSet<>();
    
    @Builder
    public ChatRoom(String clubId) {
        this.clubId = clubId;
        
    }
}
