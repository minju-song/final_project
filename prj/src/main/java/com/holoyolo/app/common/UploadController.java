package com.holoyolo.app.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

	@Value("${file.upload.path}")
	private String uploadPath;

	/**
	 * 이미지 파일 업로드
	 * @param uploadFiles
	 * @return
	 */
	@PostMapping("/uploadAjax2")
	@ResponseBody
	public List<String> uploadFile(@RequestPart MultipartFile[] uploadFiles, String folderName) { // @RequestPart MultipartFile[배열로
																				// 받는다(여러파일 동시에)] 파일 업로드 어노테이션
																				// multipart/form-data로 넘어온 데이터 처리
		String folder = "\"" + folderName;

		List<String> fileList = new ArrayList<>();

		for (MultipartFile uploadFile : uploadFiles) {
			
			// 이미지파일만 가능하도록 제한.
	    	if(uploadFile.getContentType().startsWith("image") == false){
	    		System.err.println("this file is not image type");
	    		return null;
	        }
			
			// 원래이름과 저장할이름(파일명 중복 때문)
			String originalName = uploadFile.getOriginalFilename();
			String fileName = originalName.substring(originalName.lastIndexOf("//") + 1);

			System.out.println("원본 파일명 : " + originalName);
			System.out.println("저장 파일명 : " + fileName);

			// 1. 날짜 폴더 생성(파일 관리를 편리하게 하기 위해서)
			String folderPath = makeFolder();
			String uuid = UUID.randomUUID().toString(); // UUID(랜덤값) : 저장할 파일 이름 중간에 "_"를 이용하여 구분
			// 2. 업로드할 파일 이름
			String uploadFileName = folderPath + File.separator + uuid + "_" + fileName;
			// 3. 저장할 경로를 문자로 저장 (1+2)
			String saveName = uploadPath + File.separator + uploadFileName;
			// 4. 문자로 저장된 경로를 처리
			Path savePath = Paths.get(saveName); //Paths.get() 메서드는 특정 경로의 파일 정보를 가져옵니다.(경로 정의하기)
			
			System.out.println("path : " + saveName);
			
			try {
				uploadFile.transferTo(savePath); // uploadFile에 파일을 업로드 하는 메서드 transferTo(file), 매개변수 : Path타입!!
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			// DB에 해당 경로 저장
			// 1) 사용자가 업로드할 때 사용한 파일명
			// 2) 실제 서버에 업로드할 때 사용한 경로
			String saveFile = setImagePath(uploadFileName);
			fileList.add(saveFile); // File.separator를 "/"로 변환
		}

		return fileList;
	}

	/**
	 * 폴더 생성
	 * @return
	 */
	private String makeFolder() {
		String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		// LocalDate를 문자열로 포멧
		String folderPath = str.replace("/", File.separator); // (중요) File.separator : 실제 경로로 인식하는 / !!!
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

	/**
	 * 브라우저에서 사용하는 경로를 반환
	 * @param uploadFileName
	 * @return
	 */
	private String setImagePath(String uploadFileName) { // 화면(브라우저)에서 사용하는 경로를 반환..
		return uploadFileName.replace(File.separator, "/");
	}

}