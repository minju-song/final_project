package com.holoyolo.app.attachment.mapper;

import java.util.List;

import com.holoyolo.app.attachment.service.AttachmentVO;


public interface AttachmentMapper {

	//중고거래 이미지 전체조회
	public List<AttachmentVO> selectAttachmentList(AttachmentVO attachmentVO);
}
