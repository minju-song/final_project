package com.holoyolo.app.accBookHistory.service;

import lombok.Data;

@Data
public class AccBookHistoryVO {
	private int abHistoryId;
	private String inputOutput;
	private String paymentType;
	private String bankname;
	private int price;
	private String payStore;
	private String accBookComment;
	private String memberId;
}
