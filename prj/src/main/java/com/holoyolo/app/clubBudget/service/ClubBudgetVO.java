package com.holoyolo.app.clubBudget.service;

import java.util.Date;

import lombok.Data;

@Data
public class ClubBudgetVO {
	private int clubBudgetId;
	private int clubBudgetPrice;
	private String clubBudgetUnit;
	private Date settingDate;
	private int clubId;
	private char userYn;
}
