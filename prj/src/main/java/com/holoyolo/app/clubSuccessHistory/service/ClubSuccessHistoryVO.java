package com.holoyolo.app.clubSuccessHistory.service;

import java.util.Date;

import lombok.Data;

@Data
public class ClubSuccessHistoryVO {
	private int goalId;
	private Date startDate;
	private Date endDate;
	private String successState;
	private double successPct;
	private int clubBudgetId;
}
