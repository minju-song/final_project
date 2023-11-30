package com.holoyolo.app.editor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EditorApiRestController {
	
	// 파일을 업로드할 디렉터리 경로
	@Value("${file.upload.path}")
    private String uploadDir;

	@Autowired
    private final PostService postService;
	
	
	/**
     * 에디터에서 이미지를 업로드했을 때 'blob'으로 넘어오는 File 객체를 이용해서 디스크에 파일을 저장합니다.
     * @param image 파일 객체
     * @return 업로드된 파일명
     */
    @PostMapping("/tui-editor/image-upload")
    public String uploadEditorImage(@RequestParam MultipartFile image) {
    	
        if (image.isEmpty()) {
            return "";
        }

        // 원래이름과 저장할이름(파일명 중복 때문)
        String originalName = image.getOriginalFilename();
        String fileName = originalName.substring(originalName.lastIndexOf("//")+1);
        
        System.out.println("fileName : " + fileName);
        
        //날짜 폴더 생성(파일 관리를 편리하게 하기 위해서)
        String folderPath = makeFolder();
        String uuid = UUID.randomUUID().toString(); //UUID(랜덤값)
        
        // 업로드할 파일 이름 - 저장할 파일 이름 중간에 "_"를 이용하여 구분
        String uploadFileName = folderPath +File.separator + uuid + "_" + fileName;
        
        // 실제 저장할 경로를 문자로 저장
        String saveName = uploadDir + File.separator + uploadFileName;
        
        Path savePath = Paths.get(saveName); // 문자로 저장된 경로를 처리
        //Paths.get() 메서드는 특정 경로의 파일 정보를 가져옵니다.(경로 정의하기)
        System.out.println("path : " + saveName);
        
        try{
        	// uploadFile에 파일을 업로드 하는 메서드 transferTo(file)
        	// 문자열을 넘기면안된다!!!!! Path타입이여야함.
        	image.transferTo(savePath);
        } catch (IOException e) {
             e.printStackTrace();	             
        }
        // DB에 해당 경로 저장
        // 1) 사용자가 업로드할 때 사용한 파일명
        // 2) 실제 서버에 업로드할 때 사용한 경로
        
        String saveFilename = setImagePath(uploadFileName);
        
        return saveFilename;
    }
    
    /**
     * 폴더를 만든다
     * @return 폴더경로
     */
    private String makeFolder() {
		String str = "editor/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		// LocalDate를 문자열로 포멧
		String folderPath = str.replace("/", File.separator); // (중요) File.separator : 실제 경로로 인식하는 / !!!
		File uploadPathFoler = new File(uploadDir, folderPath);
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
    private String setImagePath(String uploadFileName) { //화면(브라우저)에서 사용하는 경로를 반환..
		return uploadFileName.replace(File.separator, "/");
	}

    /**
     * 디스크에 저장된 이미지 파일을 byte array로 변환해서 에디터에 이미지를 렌더링 합니다.
     * @param filename 디스크에 업로드된 파일명
     * @return image byte array
     */
    @GetMapping(value = "/tui-editor/image-print", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public byte[] printEditorImage(@RequestParam final String filename) {
        // 업로드된 파일의 전체 경로
        String fileFullPath = Paths.get(uploadDir, filename).toString();

        // 파일이 없는 경우 예외 throw
        File uploadedFile = new File(fileFullPath);
        if (uploadedFile.exists() == false) {
            throw new RuntimeException();
        }

        try {
            // 이미지 파일을 byte[]로 변환 후 반환
            byte[] imageBytes = Files.readAllBytes(uploadedFile.toPath());
            return imageBytes;

        } catch (IOException e) {
            // 예외 처리는 따로 해주는 게 좋습니다.
            throw new RuntimeException(e);
        }
    }
	
    /**
     * 에디터 게시글 저장
     * @param params
     * @return
     */
    @PostMapping("/api/posts")
    public int insertPost(@RequestBody final PostVO params) {
        return postService.insertPost(params);
    }

    /**
     * 에디터 게시글 상세조회
     * @param boardId
     * @return
     */
    @GetMapping("/api/posts/{boardId}")
    public PostVO selectPostInfo(@PathVariable final int boardId) {
        return postService.selectPostInfo(boardId);
    }

}