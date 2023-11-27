package com.holoyolo.app.member.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberVO {
	
	private String memberId;
	private String memberName;
	private String password;
	private String nickname;
	private String addr;
	private String detailAddr;
	private String phone;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date joinDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date stopDate;
	private String role;
	private String profileImg;
	private String memberIntro;
	private int reportCnt;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date freedomDate;
	private int loginFailCnt;
	private String lockYn;
	private String snsYn;
	
	private String provider;
	private String providerId;

}