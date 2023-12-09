package com.holoyolo.app.chat.service.trade;


import lombok.Builder;
import lombok.Data;

@Data
public class TradeChatRoomVO {
	
	private int tradeId;
	private String sellerId;
	private String buyerId;
	private String title;
	private int notread;
    @Builder
    public TradeChatRoomVO(int tradeId, String sellerId, String buyerId, String title) {
        this.tradeId = tradeId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.title = title;
    }

}
