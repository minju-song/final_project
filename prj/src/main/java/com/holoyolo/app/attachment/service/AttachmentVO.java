package com.holoyolo.app.attachment.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class AttachmentVO {

	private int attachmentId;
	private String menuType;
	private int postId;
	private int attachmentSeq;
	private String originFile;
	private String saveFile;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date regDate;
	private int fileSize;

}
