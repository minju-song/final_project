package com.holoyolo.app.answer.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class AnswerVO {
	private int answerId;
	private String answerContent;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date answerDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateDate;
	private int questionId;
}
