package com.holoyolo.app.attachment.service.impl;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.holoyolo.app.attachment.mapper.AttachmentMapper;
import com.holoyolo.app.attachment.service.AttachmentService;

@Service
public class AttachmentServiceImpl implements AttachmentService {

	@Autowired
	AttachmentMapper attachmentMapper;
	
	@Value("${file.upload.path}")
    private String uploadPath;

	@Override
	public String uploadImage(MultipartFile file, String type) throws IllegalStateException, IOException {
		//이미지올리는 이유 타입으로 지정해서 본인이 경로 설정
		
		if(file.getContentType().startsWith("image") == false) {
			return null;
		}
		
		
		String originalName = file.getOriginalFilename();
		String fileName = originalName.substring(originalName.lastIndexOf("//")+1);

		String folderPath = makeFolder(type);
		
		String uuid = UUID.randomUUID().toString();

		
	    
	    String uploadFileName = folderPath +File.separator + uuid + "_" + fileName;
	    
	    String saveName = uploadPath + File.separator + uploadFileName;
	    
	    Path savePath = Paths.get(saveName);
	    
	    file.transferTo(savePath);
	    
	
	    
	    return setImagePath(uploadFileName);

	}
	
	private String makeFolder(String type) {
		
		String folderPath = "";
		
		//클럽프로필
		//upload폴더 밑에 생성할 하위폴더 경로 본인들이 생성하기
		if(type.equals("clubProfile")) {
			folderPath = "club"+File.separator+"profile";
		}
		
		File uploadPathFoler = new File(uploadPath, folderPath);
		// File newFile= new File(dir,"파일명");
		if (uploadPathFoler.exists() == false) {
			uploadPathFoler.mkdirs();
			// 만약 uploadPathFolder가 존재하지않는다면 makeDirectory하라는 의미입니다.
			// mkdir(): 디렉토리에 상위 디렉토리가 존재하지 않을경우에는 생성이 불가능한 함수
			// mkdirs(): 디렉토리의 상위 디렉토리가 존재하지 않을 경우에는 상위 디렉토리까지 모두 생성하는 함수
		}
		return folderPath;
	}
	
	private String setImagePath(String uploadFileName) {
		return uploadFileName.replace(File.separator, "/");
	}

}
