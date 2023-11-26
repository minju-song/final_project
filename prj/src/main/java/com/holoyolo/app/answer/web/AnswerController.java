package com.holoyolo.app.answer.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.answer.service.AnswerService;
import com.holoyolo.app.answer.service.AnswerVO;


@Controller
public class AnswerController {
	
	@Autowired
	AnswerService answerService;
	

	
	// 답변 등록하기
	@PostMapping("/admin/question/detail")
	@ResponseBody
	public String answerInsertProcess(AnswerVO answerVO, @RequestParam("questionId") int questionId) {
		int answerId = answerService.insertAnswerInfo(answerVO);
		answerVO.setQuestionId(questionId);
		
		String path = null;
		if(answerId > -1) {
			path = "redirect:admin/question/detail?questionId="+answerId;
		}
		else {
			path = "redirect:admin/question/detail?questionId="+answerId;		
		}
		return path;
	}
}
