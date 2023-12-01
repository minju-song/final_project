package com.holoyolo.app.question.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.answer.service.AnswerService;
import com.holoyolo.app.answer.service.AnswerVO;
import com.holoyolo.app.question.mapper.QuestionMapper;
import com.holoyolo.app.question.service.QuestionService;
import com.holoyolo.app.question.service.QuestionVO;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired // 매퍼 주입
	QuestionMapper questionMapper;



	@Autowired
	AnswerService answerService;

	// 문의 조건조회
    @Override
    public List<QuestionVO> selectQuestionTotalList(QuestionVO questionVO) {
        return questionMapper.selectQuestionTotalList(questionVO);
    }

	// 문의 단건조회
	@Override
	public Map<String, Object> selectQuestionInfo(QuestionVO questionVO) {
		Map<String, Object> result = new HashMap<>();

		
		// 문의 단건정보
		QuestionVO findQuestionVO = questionMapper.selectQuestionInfo(questionVO);

		
		// 답변 전체조회
		List<AnswerVO> findAnswerVO = answerService.selectAnswerAll(questionVO);

	    result.put("questionInfo", findQuestionVO);
	    result.put("answerInfo", findAnswerVO);

		return result;
	}
	

	// 문의 개수조회
    @Override
    public int selectQuestionTotalCount(QuestionVO questionVO) {
    	System.out.println(questionMapper.selectQuestionTotalCount(questionVO));
        return questionMapper.selectQuestionTotalCount(questionVO);
    }
	
	// 문의 등록
	@Override
	public int insertQuestionInfo(QuestionVO questionVO) {
		int result = questionMapper.insertQuestionInfo(questionVO);

		if (result == 1) {
			return questionVO.getQuestionId();
		} else {
			return -1;
		}
	}

	// 문의 수정
	@Override
	public Map<String, Object> updateQuestionInfo(QuestionVO questionVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;

		int result = questionMapper.updateQuestionInfo(questionVO);
		if (result == 1) {
			isSuccessed = true;
		}

		map.put("result", isSuccessed);
		map.put("target", questionVO);

		return map;
	}

	// 문의 삭제
	@Override
	public boolean deleteQuestionInfo(int questionId) {
		int result = questionMapper.deleteQuestionInfo(questionId);

		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	// 페이징
	@Override
	public List<QuestionVO> selectCount(QuestionVO questionVO) {
		return questionMapper.selectCount(questionVO);
	}

	@Override
	public int selectTotalPagingCount(QuestionVO questionVO) {
		return questionMapper.selectTotalPagingCount(questionVO);
	}
}
