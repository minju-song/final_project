package com.holoyolo.app.clubMember.service;

import java.util.Date;

import lombok.Data;

@Data
public class ClubMemberVO {
	private int clubId;
	private String memberId;
	private Date joinDate;
	private Date stopDate;
	
	//멤버이름
	private String memberName;
	
	//현재소비금액
	private int sumPrice;
	
	//지난순위
	private int ranking;
}
