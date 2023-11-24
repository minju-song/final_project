package com.holoyolo.app.question.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.holoyolo.app.answer.service.AnswerService;
import com.holoyolo.app.answer.service.AnswerVO;
import com.holoyolo.app.question.service.QuestionService;
import com.holoyolo.app.question.service.QuestionVO;

@Controller
public class QuestionController {
	
	@Autowired
	QuestionService questionService;
	
	// 문의 전체조회
	@GetMapping("/admin/question")
	public String selectQuestionList(Model model) {
		List<QuestionVO> list = questionService.selectQuestionAll();
		model.addAttribute("questionList", list);
		return "admin/question/questionPage";
	}
	
	// 문의 단건조회
	@GetMapping("/admin/question/detail")
	public String selectQuestionInfo(QuestionVO questionVO, AnswerVO answerVO, Model model) {
		QuestionVO findQuestionVO = questionService.selectQuestionInfo(questionVO);
//		AnswerVO findAnswerVO = answerService.selectAnswerInfo(answerVO);
//		model.addAttribute("answerInfo", findAnswerVO);
		model.addAttribute("questionInfo", findQuestionVO);
		return "admin/question/questionDetail";
	}

}
