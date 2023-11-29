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
    
}
