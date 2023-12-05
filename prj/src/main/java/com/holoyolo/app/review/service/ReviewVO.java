package com.holoyolo.app.review.service;

import java.util.Date;

import lombok.Data;

@Data
public class ReviewVO {
	private int reviewId;
	private Date writeDate;
	private int star;
	private int clubId;
	private String memberId;
	private String contents;
	
	//리뷰작성자닉네임
	private String nickname;
}
