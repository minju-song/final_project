package com.holoyolo.app.clubMember.service;

import java.util.Date;

import lombok.Data;

@Data
public class ClubMemberVO {
	private int clubId;
	private String memberId;
	private Date joinDate;
	private Date stopDate;
}
