package com.holoyolo.app.memo.service;


import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.holoyolo.app.attachment.service.AttachmentVO;

import lombok.Data;

@Data
public class MemoVO {
	private int memoId;
	private int seqNo;
	private String color;
	private String content;
	private String hashTag;
	private String bookmark;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date writeDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateDate;
	private String memberId;
	private int firstNo;
	private int lastNo;
	
	List<AttachmentVO> images;
}