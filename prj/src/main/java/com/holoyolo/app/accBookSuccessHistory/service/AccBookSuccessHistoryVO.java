package com.holoyolo.app.accBookSuccessHistory.service;

import java.util.Date;

import lombok.Data;

@Data
public class AccBookSuccessHistoryVO {
	private int abshGoalId;
	private String successState;
	private Date startDate;
	private Date endDate;
	private int accBudgetId;
}
