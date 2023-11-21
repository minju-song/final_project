package com.holoyolo.app.trade.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class TradeVO {
	private int tradeId;
	private String title;
	private String description;
	private String tradeCategory;
	private int price;
	private String openKakao;
	private String tradePlace;
	private String tradeType;
	private String payment;
	private String promiseStatus;
	private int views;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date writeDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateDate;
	private int latitude;
	private int longitude;
	private String buyerId;
	private String sellerId;
}