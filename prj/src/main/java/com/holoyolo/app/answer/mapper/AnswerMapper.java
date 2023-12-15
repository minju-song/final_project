package com.holoyolo.app.answer.mapper;

import java.util.List;
import java.util.Map;

import com.holoyolo.app.answer.service.AnswerVO;
import com.holoyolo.app.question.service.QuestionVO;

public interface AnswerMapper {
	
	// 기본 CRUD
	// 문의 전체조회
	public List<AnswerVO> selectAnswerAll(QuestionVO questionVO);
	
	// 문의 단건조회
	public AnswerVO selectAnswerInfo(AnswerVO answerVO);
	
	// 문의 등록
	public int insertAnswerInfo(AnswerVO answerVO);
	
	// 문의 수정
	public int updateAnswerInfo(AnswerVO answerVO);
	
	// 문의 삭제
	public int deleteAnswerInfo(int answerId);
	
	// 추가 인터페이스 작성 ↓↓
	
	
	
}
