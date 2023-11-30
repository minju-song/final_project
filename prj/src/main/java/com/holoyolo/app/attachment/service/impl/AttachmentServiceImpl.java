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

	
	/**
	 * 이미지 업로드
	 * @param file
	 * @param type
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@Override
	public String uploadImage(MultipartFile file, String type) throws IllegalStateException, IOException {
		//이미지올리는 이유 타입으로 지정해서 본인이 경로 설정 => makeFolder(type)에서 매개변수로 사용
		
		if(file.getContentType().startsWith("image") == false) {
			return null;
		}
		
		String originalName = file.getOriginalFilename(); 							//원본 파일명
		String fileName = originalName.substring(originalName.lastIndexOf("//")+1); // 새로운 파일명
		String folderPath = makeFolder(type);
		String uuid = UUID.randomUUID().toString();
	    String uploadFileName = folderPath +File.separator + uuid + "_" + fileName; // 폴더경로 + uuid + 새로운 파일명
	    String saveName = uploadPath + File.separator + uploadFileName; 			// 실재 저장할 경로를 문자로 저장
	    
	    Path savePath = Paths.get(saveName);	// 문자로 저장된 경로를 처리
	    file.transferTo(savePath);				// 파일을 업로드, 매개변수는 반드시 Path 타입이어야한다.
	    
	    
	    return setImagePath(uploadFileName);
	}
	
	/**
	 * 폴더를 만든다
	 * @param type
	 * @return
	 */
	private String makeFolder(String type) {
		
		String folderPath = "";
		
		//upload폴더 밑에 생성할 하위폴더 경로 본인들이 생성하기
		if(type.equals("clubProfile")) { // 알뜰모임
			folderPath = "club" + File.separator + "profile";
		} else if(type.equals("myProfile")) { // 마이페이지(나의정보)
			folderPath = "mypage" + File.separator + "profile";
		}
		
		// File newFile = new File(dir,"파일명");
		File uploadPathFoler = new File(uploadPath, folderPath);
		if (uploadPathFoler.exists() == false) {
			uploadPathFoler.mkdirs();
			// 만약 uploadPathFolder가 존재하지않는다면 makeDirectory하라는 의미입니다.
			// mkdir(): 디렉토리에 상위 디렉토리가 존재하지 않을경우에는 생성이 불가능한 함수
			// mkdirs(): 디렉토리의 상위 디렉토리가 존재하지 않을 경우에는 상위 디렉토리까지 모두 생성하는 함수
		}
		return folderPath;
	}
	
	/**
	 * 화면(브라우저)에서 사용하는 경로를 반환(DB에 저장)
	 * @param uploadFileName
	 * @return
	 */
	private String setImagePath(String uploadFileName) {
		return uploadFileName.replace(File.separator, "/");
	}

}
