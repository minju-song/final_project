package com.holoyolo.app.attachment.service.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.holoyolo.app.attachment.mapper.AttachmentMapper;
import com.holoyolo.app.attachment.service.AttachmentService;
import com.holoyolo.app.attachment.service.AttachmentVO;
import com.holoyolo.app.member.service.MemberVO;
import com.holoyolo.app.board.service.BoardVO;


@Service
public class AttachmentServiceImpl implements AttachmentService {

	@Autowired
	AttachmentMapper attachmentMapper;

	@Value("${file.upload.path}")
	private String uploadPath;

	/**
	 * 이미지 업로드
	 * 
	 * @param file
	 * @param type
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@Override
	public String uploadImage(MultipartFile file, String type) throws IllegalStateException, IOException {
		// 이미지올리는 이유 타입으로 지정해서 본인이 경로 설정 => makeFolder(type)에서 매개변수로 사용

		if (file.getContentType().startsWith("image") == false) {
			return null;
		}

		String originalName = file.getOriginalFilename(); // 원본 파일명
		String fileName = originalName.substring(originalName.lastIndexOf("//") + 1); // 새로운 파일명
		String folderPath = makeFolder(type);
		String uuid = UUID.randomUUID().toString();
		String uploadFileName = folderPath + File.separator + uuid + "_" + fileName; // 폴더경로 + uuid + 새로운 파일명
		String saveName = uploadPath + File.separator + uploadFileName; // 실재 저장할 경로를 문자로 저장

		Path savePath = Paths.get(saveName); // 문자로 저장된 경로를 처리
		file.transferTo(savePath); // 파일을 업로드, 매개변수는 반드시 Path 타입이어야한다.

		return setImagePath(uploadFileName);
	}

	/**
	 * 중고거래 첨부파일 목록 가져오기
	 */
	@Override
	public List<AttachmentVO> getAttachmentList(AttachmentVO attachmentVO) {
		return attachmentMapper.selectAttachmentList(attachmentVO);
	}

	/**
	 * 첨부파일 여러개
	 * 
	 * @param uploadFiles
	 * @return
	 */
	public List<AttachmentVO> uploadFiles(MultipartFile[] uploadFiles, String type) { 

		List<AttachmentVO> imageList = new ArrayList<>();
		if(uploadFiles != null) {
			for (MultipartFile uploadFile : uploadFiles) {
				AttachmentVO vo = new AttachmentVO();
	
				if (type.equals("trade") || type.equals("memo")) {
					// 이미지파일만 가능하도록 제한.
					if (uploadFile.getContentType().startsWith("image") == false) {
						System.err.println("this file is not image type");
						return null;
					}
				}
	
				if (type.equals("notice")) {
					if (uploadFile.isEmpty()) {
						System.err.println("this file is not image type");
						return null;
					}
				}
	
				if (type.equals("noticeAttach")) {
					if (uploadFile.isEmpty()) {
						System.err.println("this file is not image type");
						return null;
					}
				}
	
				// 원래이름과 저장할이름(파일명 중복 때문)
				String originalName = uploadFile.getOriginalFilename();
				String fileName = originalName.substring(originalName.lastIndexOf("//") + 1);
	
				System.out.println("fileName : " + fileName);
	
				// 날짜 폴더 생성(파일 관리를 편리하게 하기 위해서)
				String folderPath = makeFolder(type);
				// UUID(랜덤값)
				String uuid = UUID.randomUUID().toString();
				// 저장할 파일 이름 중간에 "_"를 이용하여 구분
	
				// 업로드할 파일 이름
				String uploadFileName = folderPath + File.separator + uuid + "_" + fileName;
	
				// 실재 저장할 경로를 문자로 저장
				String saveName = uploadPath + File.separator + uploadFileName;
	
				Path savePath = Paths.get(saveName); // 문자로 저장된 경로를 처리
				// Paths.get() 메서드는 특정 경로의 파일 정보를 가져옵니다.(경로 정의하기)
				System.out.println("path : " + saveName);
				try {
					uploadFile.transferTo(savePath); // 문자열을 넘기면안된다!!!!! Path타입이여야함.
					// uploadFile에 파일을 업로드 하는 메서드 transferTo(file)
				} catch (IOException e) {
					e.printStackTrace();
				}
				// DB에 해당 경로 저장
				// 1) 사용자가 업로드할 때 사용한 파일명
				// 2) 실제 서버에 업로드할 때 사용한 경로
				vo.setFileSize(uploadFile.getSize());
				vo.setOriginFile(originalName);
				vo.setSaveFile(setImagePath(uploadFileName));
				imageList.add(vo); // File.separator를 "/"로 변환
			}
		}

		return imageList;
	}

	/**
	 * 폴더를 만든다
	 * 
	 * @param type
	 * @return
	 */
	private String makeFolder(String type) {

		String folderPath = "";

		// upload폴더 밑에 생성할 하위폴더 경로 본인들이 생성하기
		if (type.equals("clubProfile")) { // 알뜰모임
			folderPath = "club" + File.separator + "profile";
		} else if (type.equals("myProfile")) { // 마이페이지(나의정보)
			folderPath = "mypage" + File.separator + "profile";
		} else if (type.equals("trade")) {
			folderPath = "trade" + File.separator + "product";
		} else if (type.equals("notice")) {
			folderPath = "cs" + File.separator + "notice";
		} else if (type.equals("memo")) {
			folderPath = "memo" + File.separator + "images";
		} else if (type.equals("noticeAttach")) {
			folderPath = "cs" + File.separator + "noticeAttach";
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
	 * 
	 * @param uploadFileName
	 * @return
	 */
	private String setImagePath(String uploadFileName) {
		return uploadFileName.replace(File.separator, "/");
	}

	@Override
	public int insertAttachment(AttachmentVO attachmentVO) {
		return attachmentMapper.insertAttachment(attachmentVO);
	}

	@Override
	public int deleteAttachment(AttachmentVO attachmentVO) {
		return attachmentMapper.deleteAttachment(attachmentVO);
	}

	@Override
	public int deleteFiles(List<AttachmentVO> list) {
		for (AttachmentVO vo : list) {
			File file = new File(uploadPath + "/" + vo.getSaveFile());

			if (file.exists()) {
				file.delete();
			} else {
				System.out.println("삭제안됨==" + vo);
			}
		}
		return 0;
	}
  
	@Override
	public Map<String, List<AttachmentVO>> getCSAttachmentList(AttachmentVO attachmentVO) {
		List<AttachmentVO> sourceList = attachmentMapper.selectAttachmentList(attachmentVO);

		Map<String, List<AttachmentVO>> resultMap = new HashMap<>();
		List<AttachmentVO> attachList = new ArrayList<>();
		List<AttachmentVO> imgList = new ArrayList<>();
		String[] imgExtension = { "png", "jpg", "jpeg", "gif" };
		for (AttachmentVO attach : sourceList) {
			String originalFile = attach.getOriginFile();
			int lastDotIndex = originalFile.lastIndexOf(".");
			if (lastDotIndex > 0) {
				boolean imgCheck = false;
				String thisExtension = originalFile.substring(lastDotIndex + 1);
				for (String ext : imgExtension) {
					if (thisExtension.equals(ext)) {
						imgList.add(attach);
						System.out.println(attach);
						resultMap.put("imgList", imgList);
						imgCheck = true;
					}
				}
				if (!imgCheck) {
					attachList.add(attach);
					resultMap.put("attachList", attachList);
				}
			}
		}

		return resultMap;
	}

	@Override
	public int deletePostAttachment(BoardVO boardVO) {
		AttachmentVO attachmentVO = new AttachmentVO();
		attachmentVO.setPostId(boardVO.getBoardId());
		attachmentVO.setMenuType(boardVO.getMenuType());
		return attachmentMapper.deletePostAttachment(attachmentVO);
	}

	// 파일 업로드

}
