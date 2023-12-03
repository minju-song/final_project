package com.holoyolo.app.report.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ReportVO {
	private int reportId;
	private String menuType;
	private int postId;
	private String reportType;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mi")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date reportDate;
	private String reportProcessType;
	private String processComment;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mi")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date processDate;
	private String reporterId;
	private String reportContent;
	
	// 페이징
	private int page=1;
	private int rn;
	private int pageUnit=8;
	
}