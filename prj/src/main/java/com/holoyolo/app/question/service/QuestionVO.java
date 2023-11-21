package com.holoyolo.app.question.service;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class QuestionVO {
	private int question_id;
	private String question_type;
	private String title;
	private String content;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date write_date;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date update_date;
}
