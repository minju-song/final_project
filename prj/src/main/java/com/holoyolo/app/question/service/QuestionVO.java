package com.holoyolo.app.question.service;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class QuestionVO {
	private int questionId;
	private String questionType;
	private String title;
	private String content;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mi")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date writeDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mi")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date updateDate;
	private String questionYn;
	private String memberId;
	
	// 검색
	private String search;
	
	// 페이징
	private int page;
	private int rn;
	private int pageUnit;
}
