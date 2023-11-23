package com.holoyolo.app.question.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.question.service.QuestionService;
import com.holoyolo.app.question.service.QuestionVO;

@Controller
public class QuestionController {
	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	MemberService memberService;
	

	@GetMapping("/admin/question")
	public String selectQuestionList(Model model) {
		List<QuestionVO> list = questionService.selectQuestionAll();
		// List<MemberVO> list = memberService.
		model.addAttribute("questionList", list);
		return "admin/questionMgt";
	}
	
//	@GetMapping("/adminQuestionWaiting")
//	public String selectQuestionWaitingList(Model model) {
//		List<QuestionVO> list = questionService.selectQuestionWaiting();
//		model.addAttribute("questionList", list);
//	}
}
