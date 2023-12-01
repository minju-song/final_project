package com.holoyolo.app.attachment.service;

import java.io.IOException;

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
}
