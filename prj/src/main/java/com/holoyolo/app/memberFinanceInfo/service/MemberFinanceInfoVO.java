package com.holoyolo.app.memberFinanceInfo.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class MemberFinanceInfoVO {
	
	private int memberFinanceId;
	private String memberId;
	private String bankname;
	private String account;
	private String cardCompany;
	private String cardNo;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date settingDate;
	private String userYn;

}
