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
	//검색어
	private String search;
	//검색주제
	private String searchTitle;
	//리더 이름
	private String leaderName;
	//클럽 가입자수
	private int joinCnt;

}
