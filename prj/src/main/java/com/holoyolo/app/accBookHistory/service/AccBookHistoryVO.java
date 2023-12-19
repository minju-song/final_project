package com.holoyolo.app.accBookHistory.service;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

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
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate payDate;
	private String payDateStr;
	
	//차트용 데이터
	private int inputSum;
	private int outputSum;
	private int inputOutputSum;
	private String payDateMonth;
}
