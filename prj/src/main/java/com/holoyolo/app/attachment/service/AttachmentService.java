package com.holoyolo.app.attachment.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

	/**
	 * 이미지 업로드
	 * @param file
	 * @param type
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public String uploadImage(MultipartFile file, String type) throws IllegalStateException, IOException;
	
	//중고거래 이미지 전체조회
	public List<AttachmentVO> getAttachmentList(AttachmentVO attachmentVO);
}
