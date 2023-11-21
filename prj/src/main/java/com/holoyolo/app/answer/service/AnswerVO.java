package com.holoyolo.app.answer.service;

import java.util.Date;

import lombok.Data;

@Data
public class AnswerVO {
	private int answerId;
	private String answerContent;
	private Date answerDate;
	private Date updateDate;
	private int questionId;
}
