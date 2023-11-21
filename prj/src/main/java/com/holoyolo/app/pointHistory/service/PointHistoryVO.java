package com.holoyolo.app.pointHistory.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class PointHistoryVO {
	
	private int pHistoryId;
	private String memberId;
	private String pointType;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date pointDate;
	private int price;
	private int clubId;
	private int tradeId;

}
