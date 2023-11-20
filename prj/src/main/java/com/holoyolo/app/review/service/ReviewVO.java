package com.holoyolo.app.review.service;

import java.util.Date;

import lombok.Data;

@Data
public class ReviewVO {
	private int reviewId;
	private Date writeDate;
	private int Star;
	private int clubId;
	private String memberId;
}
