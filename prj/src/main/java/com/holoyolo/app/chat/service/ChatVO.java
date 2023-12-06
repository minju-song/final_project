package com.holoyolo.app.chat.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ChatVO {

    private String memberId;
    private String msg;
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date realDate;
    private String date;
    private int clubId;
    private String memberName;
    private String type;
    private int chatId;
}
