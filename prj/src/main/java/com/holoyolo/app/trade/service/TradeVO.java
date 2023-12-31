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
	private String paymentType;
	private String promiseStatus;
	private int views;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date writeDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateDate;
	private Double latitude;
	private Double longitude;
	private String buyerId;
	private String sellerId;
	
	//페이징
	private int page;
	//검색어
	private String search;
	//검색주제
	private String searchTitle;
	//판매완료 제외 체크
	private String sellCheck;
	
	private String nickname;
	private String memberIntro;
	
	private String profileImg;
	private String tradeCategoryName;
	private String tradeTypeName;
	private String paymentTypeName;
	private String promiseStatusName;
	
	private String saveFile;
	
	private String listType;
	private String memberId;
}