package com.holoyolo.app.question.service;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class QuestionVO {
	private int questionㅑd;
	private String questionType;
	private String title;
	private String content;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date writeDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateDate;
}
