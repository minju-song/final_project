package com.holoyolo.app.club.service;

import lombok.Data;

@Data
public class ClubVO {
	private int clubId;
	private String clubName;
	private String clubIntro;
	private String clubProfileImg;
	private int clubPeople;
	private String joinCondition;
	private String openScope;
	private String clubLeader;
	private String clubCasher;

}
