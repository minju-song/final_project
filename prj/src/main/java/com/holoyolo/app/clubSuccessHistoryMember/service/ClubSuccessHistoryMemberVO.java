package com.holoyolo.app.clubSuccessHistoryMember.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ClubSuccessHistoryMemberVO {
	private int goalMemberId;
	private int ranking;
	private String memberId;
	private int goal_id;
	
	//시작날짜
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	//끝날짜
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
}
