package com.holoyolo.app.attachment.mapper;

import java.util.List;

import com.holoyolo.app.attachment.service.AttachmentVO;


public interface AttachmentMapper {

	//중고거래 이미지 전체조회
	public List<AttachmentVO> selectAttachmentList(AttachmentVO attachmentVO);
	
	//첨부파일 등록
	public int insertAttachment(AttachmentVO attachmentVO);
	
	//첨부파일 삭제
	public int deleteAttachment(AttachmentVO attachmentVO);
}
