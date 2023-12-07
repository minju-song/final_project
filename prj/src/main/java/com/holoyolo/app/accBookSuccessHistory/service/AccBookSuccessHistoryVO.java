package com.holoyolo.app.accBookSuccessHistory.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class AccBookSuccessHistoryVO {
	private int abshGoalId;
	private String successState;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	private int accBudgetId;
	
	//임의로 회원아이디 추가
	private String memberId;
	
	//해당 기간동안 쓴 소비금액
	private int sumPrice;
	
	private int goalPrice;
	
	private String goalUnit;
}
