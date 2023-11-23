package com.holoyolo.app.question.service;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class QuestionVO {
	private int questionId;
	private String questionType;
	private String title;
	private String content;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date writeDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateDate;
	private String questionYn;
}
