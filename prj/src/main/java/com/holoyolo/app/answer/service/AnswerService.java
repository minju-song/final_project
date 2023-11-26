package com.holoyolo.app.answer.service;

import java.util.List;
import java.util.Map;

import com.holoyolo.app.question.service.QuestionVO;

public interface AnswerService {
	
	// 기본 CRUD
	// 문의 전체조회
	public List<AnswerVO> selectAnswerAll(QuestionVO questionVO);
	
	// 문의 단건조회
	public AnswerVO selectAnswerInfo(AnswerVO answerVO);
	
	// 문의 등록
	public int insertAnswerInfo(AnswerVO answerVO);
	
	// 문의 수정
	public Map<String, Object> updateAnswerInfo(AnswerVO answerVO);
	
	// 문의 삭제
	public boolean deleteAnswerInfo(int answerId);
	
	// 추가 인터페이스 작성 ↓↓
	
}
