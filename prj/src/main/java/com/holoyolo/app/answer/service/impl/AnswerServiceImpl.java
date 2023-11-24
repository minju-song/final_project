package com.holoyolo.app.answer.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.answer.mapper.AnswerMapper;
import com.holoyolo.app.answer.service.AnswerService;
import com.holoyolo.app.answer.service.AnswerVO;

@Service
public class AnswerServiceImpl implements AnswerService {
	
	@Autowired
	AnswerMapper answerMapper;

	@Override
	public List<AnswerVO> selectAnswerAll() {
		return answerMapper.selectAnswerAll();
	}

	@Override
	public AnswerVO selectAnswerInfo(AnswerVO answerVO) {
		return answerMapper.selectAnswerInfo(answerVO);
	}

	@Override
	public int insertAnswerInfo(AnswerVO answerVO) {
		int result = answerMapper.insertAnswerInfo(answerVO);
		
		if (result == 1) {
			return answerVO.getQuestionId();
		} else {
			return -1;
		}
	}

	@Override
	public Map<String, Object> updateAnswerInfo(AnswerVO answerVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;
		
		int result = answerMapper.updateAnswerInfo(answerVO);
		if (result == 1) {
			isSuccessed = true;
		}
		
		map.put("result", isSuccessed);
		map.put("target", answerVO);
		
		return map;
	}

	@Override
	public boolean deleteAnswerInfo(int answerId) {
		int result = answerMapper.deleteAnswerInfo(answerId);
		
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}
}
