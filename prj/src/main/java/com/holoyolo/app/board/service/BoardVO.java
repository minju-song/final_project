package com.holoyolo.app.board.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class BoardVO {

	private int boardId;
	private String menuType;
	private String title;
	private String content;
	@DateTimeFormat(pattern = "yyMMdd")
	private Date writeDate;
	private Date updateDate;
	private int views;
	private String memberId;
	private int clubId;
	
	
	private int likeCount;
	private String nickname;
	

	private int page;
	private int rnum;

	private String searchWord;
	private String searchOption;
	
	
}
