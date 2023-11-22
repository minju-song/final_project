package com.holoyolo.app.question.mapper;

import java.util.List;
import java.util.Map;

import com.holoyolo.app.question.service.QuestionVO;

public interface QuestionMapper {

	// 기본 CRUD
	// 문의 전체조회
	public List<QuestionVO> selectQuestionAll();
	
	// 문의 단건조회
	public QuestionVO selectQuestionInfo(QuestionVO questionVO);
	
	// 문의 등록
	public int insertQuestionInfo(QuestionVO questionVO);
	
	// 문의 수정
	public Map<String, Object> updateQuestionInfo(QuestionVO questionVO);
	
	// 문의 삭제
	public boolean deleteQuestionInfo(int questionId);
	
	// 추가 인터페이스 작성 ↓↓
}
