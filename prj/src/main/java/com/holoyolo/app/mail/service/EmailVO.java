package com.holoyolo.app.mail.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailVO {

	private String to;		// 수신자
    private String subject;	// 제목
    private String message;	// 내용
    
    private int clubId; //클럽아이디
    private String reqId; //가입자아이디
    private String text; //가입사유
    private String clubName; //클럽이름
    private String type; //가입타입
    
}
