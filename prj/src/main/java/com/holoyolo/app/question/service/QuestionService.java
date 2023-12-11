package com.holoyolo.app.question.service;

import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;

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
	
	public Page<QuestionVO> MyQuestionList(Pageable pageable, String string);
    
	
}
