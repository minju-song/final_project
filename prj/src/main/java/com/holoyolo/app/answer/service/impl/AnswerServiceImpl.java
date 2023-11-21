package com.holoyolo.app.answer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.answer.mapper.AnswerMapper;
import com.holoyolo.app.answer.service.AnswerService;

@Service
public class AnswerServiceImpl implements AnswerService {
	
	@Autowired
	AnswerMapper answerMapper;
}
