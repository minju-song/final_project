package com.holoyolo.app.question.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.question.mapper.QuestionMapper;
import com.holoyolo.app.question.service.QuestionService;
import com.holoyolo.app.question.service.QuestionVO;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired // 매퍼 주입
	QuestionMapper questionMapper;

	// 문의 전체조회
	@Override
	public List<QuestionVO> selectQuestionAll() {
		return questionMapper.selectQuestionAll();
	}

	// 문의 단건조회
	@Override
	public QuestionVO selectQuestionInfo(QuestionVO questionVO) {
		return questionMapper.selectQuestionInfo(questionVO);
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

}
