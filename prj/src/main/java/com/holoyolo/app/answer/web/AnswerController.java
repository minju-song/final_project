package com.holoyolo.app.answer.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.holoyolo.app.answer.service.AnswerService;


@Controller
public class AnswerController {
	
	@Autowired
	AnswerService answerService;
	

	
//	// 답변 등록하기
//	@PostMapping("answerInsert")
//	public String answerInsertProcess(AnswerVO answerVO) {
//		answerService.insertAnswerInfo(answerVO);
//		return "redirect:/admin/question/detail";
//	}
}
