package com.holoyolo.app.chat.service.trade;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.holoyolo.app.member.service.MemberVO;

import lombok.Builder;
import lombok.Data;

@Data
public class TradeChatRoomVO {
	
	private int tradeId;
	private String sellerId;
	private String buyerId;
	private String title;
	private int notread;
	private String lastchat;
	private String promiseStatus;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date lastChatTime;
	
	//상대정보
	private MemberVO you;
    @Builder
    public TradeChatRoomVO(int tradeId, String sellerId, String buyerId, String title) {
        this.tradeId = tradeId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.title = title;
    }

}
