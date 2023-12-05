package com.holoyolo.app.chat.service;

import lombok.Data;

@Data
public class ChatVO {
    // 메시지 타입 : 입장, 채팅, 나감
    public enum MessageType {
        ENTER, TALK,QUIT
    }
    private MessageType type; // 메시지 타입
    private String clubId; // 방번호
    private String sender; // 메시지 보낸사람
    private String message; // 메시지
}
