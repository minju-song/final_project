package com.holoyolo.app.answer.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.holoyolo.app.answer.service.AnswerService;

@Controller
public class AnswerController {
	
	@Autowired
	AnswerService answerService;
}
