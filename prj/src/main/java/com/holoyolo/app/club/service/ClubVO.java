package com.holoyolo.app.club.service;

import java.util.Date;

import lombok.Data;

@Data
public class ClubVO {
	private int clubId;
	private String clubName;
	private String clubIntro;
	private String clubProfileImg;
	private int clubPeople;
	private Date clubDate;
	private String joinCondition;
	private String openScope;
	private String clubLeader;
	private String clubCasher;

	
	//페이징
	private int page;


}
