package com.holoyolo.app.attachment.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.holoyolo.app.board.service.BoardVO;

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
	
	/**
	 * 다중 업로드
	 * @param uploadFiles
	 * @param type
	 * @return
	 */
	public List<AttachmentVO> uploadFiles(MultipartFile[] uploadFiles, String type);
	
	/**
	 * 첨부파일 등록
	 * @param attachmentVO
	 * @return
	 */
	public int insertAttachment(AttachmentVO attachmentVO);
	
	//중고거래 이미지 전체조회
	public List<AttachmentVO> getAttachmentList(AttachmentVO attachmentVO);
	
	//첨부파일 삭제
	public int deleteAttachment(AttachmentVO attachmentVO);
	//고객센터 첨부 조회
	public Map<String, List<AttachmentVO>> getCSAttachmentList(AttachmentVO attachmentVO);
	
	
	//업데이트 전 첨부파일 초기화
	public int deletePostAttachment(BoardVO boardVO);
}
