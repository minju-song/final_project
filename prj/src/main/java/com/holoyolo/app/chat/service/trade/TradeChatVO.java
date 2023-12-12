package com.holoyolo.app.chat.service.trade;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class TradeChatVO {
	private int tradeChatId;
	private int tradeChatRoomId;
    private String memberId;
    private String msg;
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date realDate;
    private String date;
    private String dateStr;

    private String memberName;
    private String type;
    private int tradeId;
}
