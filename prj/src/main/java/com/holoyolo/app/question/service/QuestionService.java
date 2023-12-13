package com.holoyolo.app.question.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.holoyolo.app.attachment.service.AttachmentVO;


public interface QuestionService {
	
	// 기본 CRUD
	// 문의 조건조회
	public List<QuestionVO> selectQuestionTotalList(QuestionVO questionVO);
	
	// 문의 단건조회
	public Map<String, Object> selectQuestionInfo(QuestionVO questionVO);
	
	// 문의 개수조회
	public int selectQuestionTotalCount(QuestionVO questionVO);
	
	// 문의 등록
	public int insertQuestionInfo(QuestionVO questionVO);
	
	// 문의 수정
	public Map<String, Object> updateQuestionInfo(QuestionVO questionVO);
	
	// 문의 삭제
	public boolean deleteQuestionInfo(int questionId);

	// 추가 인터페이스 작성 ↓↓
	// 페이징
	public int selectTotalPagingCount(QuestionVO questionVO);
	
	
	//회원 문의 리스트
	 
	public Page<QuestionVO> MyQuestionList(String memberId, Pageable pageable);
	//회원 문의 개수
	public int myQuestionCnt(String string);
    
	//문의 등록
	
	public int insertQuestion(QuestionVO questionVO, List<AttachmentVO> imgList, List<AttachmentVO> attachList);

	public QuestionVO selectQuestion(int questionId);
	
	public int updateQuestion(QuestionVO questionVO, List<AttachmentVO> imgList, List<AttachmentVO> attachList);
}
