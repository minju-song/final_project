package com.holoyolo.app.attachment.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

	public String uploadImage(MultipartFile file, String type) throws IllegalStateException, IOException;
}
