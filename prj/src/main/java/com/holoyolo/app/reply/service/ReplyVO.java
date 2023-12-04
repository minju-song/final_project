package com.holoyolo.app.reply.service;

import java.util.Date;

import lombok.Data;

@Data
public class ReplyVO {

	private int replyId;
	private int upperReplyId;
	private String content;
	private Date writeDate;
	private Date updateDate;
	private int boardId;
	private String memberId;
	
	private String nickname;
}
