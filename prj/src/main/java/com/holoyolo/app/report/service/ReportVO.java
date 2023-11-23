package com.holoyolo.app.report.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ReportVO {
	private int reportId;
	private String menuType;
	private int postId;
	private String reportType;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date reportDate;
	private String reportProcessType;
	private String processComment;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date processDate;
}
