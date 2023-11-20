package com.holoyolo.app.accBudget.service;

import java.util.Date;

import lombok.Data;

@Data
public class AccBudgetVO {
	private int accBudgetId;
	private int accBudgetPrice;
	private String accBudgetUnit;
	private Date settingDate;
	private String memberId;
	private char useYn;
}
