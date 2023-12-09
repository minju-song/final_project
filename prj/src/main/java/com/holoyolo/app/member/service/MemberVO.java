package com.holoyolo.app.member.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberVO {
	// 회원
	private String memberId;
	private String memberName;
	private String password;
	private String nickname;
	private String addr;
	private String detailAddr;
	private String phone;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date joinDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date stopDate;
	private String role;
	private String profileImg;
	private String memberIntro;
	private Integer reportCnt = -1;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date freedomDate;
	private int loginFailCnt;
	private String lockYn;
	private String snsYn;
	
	private String provider;
	private String providerId;
	private String delMember;
	private String authNum;
	private String vResult;

	// 관리자
	// 검색
	private String search;
	// 페이징
	private int page=1;
	private int rn;
	private int pageUnit=10;
	// 홀로페이
	private int pointPrice;
	private int holopayPrice;
	
	//포인트, 홀로페이 등록
	private int point;
	private int tradeId;
	private int holoPay;
}