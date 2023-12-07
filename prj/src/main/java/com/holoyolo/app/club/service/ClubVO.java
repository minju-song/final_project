package com.holoyolo.app.club.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ClubVO {
	private int clubId;
	private String clubName;
	private String clubIntro;
	private String clubProfileImg;
	private int clubPeople;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date clubDate;
	private String joinCondition;
	private String openScope;
	private String clubLeader;
	private String clubCasher;

	
	//검색주제
	private String searchTitle;
	//리더 이름
	private String leaderName;
	//클럽 가입자수
	private int joinCnt;
	//클럽 성공횟수
	private int successCnt;
	//가입요청메시지
	private String text;
	
	//이미지처리
	private MultipartFile img;
	
	//예산기간
	private String unit;
	//예산금액
	private int price;
	
	//가입타입 (최초가입, 재가입)
	private String type;
	
	// 검색
	private String search;
	
	// 페이징
	private int page;
	private int rn;
	private int pageUnit;

}
