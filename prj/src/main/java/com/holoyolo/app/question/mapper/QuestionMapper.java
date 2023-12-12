package com.holoyolo.app.question.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;

import com.holoyolo.app.question.service.QuestionVO;

public interface QuestionMapper {

	// 기본 CRUD
	// 문의 조건조회
	public List<QuestionVO> selectQuestionTotalList(QuestionVO questionVO);

	// 문의 단건조회
	public QuestionVO selectQuestionInfo(QuestionVO questionVO);

	// 문의 개수조회
	public int selectQuestionTotalCount(QuestionVO questionVO);

	// 문의 등록
	public int insertQuestionInfo(QuestionVO questionVO);

	// 문의 수정
	public int updateQuestionInfo(QuestionVO questionVO);

	// 문의 삭제
	public int deleteQuestionInfo(int questionId);

	// 추가 인터페이스 작성 ↓↓
	// 페이징
	public int selectTotalPagingCount(QuestionVO questionVO);

	// 회원 인터페이스↓↓

	public List<QuestionVO> MyQuestionList(String memberId, int offset, int limit);

	public int myQuestionCnt(String memberId);

	public QuestionVO selectQuestion(int questionId);

}
