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
	
	@Autowired
	AnswerService answerService;
	
	// 문의 전체조회
	@GetMapping("/admin/question")
	public String selectQuestionList(Model model) {
		List<QuestionVO> list = questionService.selectQuestionAll();
		model.addAttribute("questionList", list);
		return "admin/question/questionPage";
	}
	
	// 문의 단건조회
	@GetMapping("/admin/question/detail")
	public String selectQuestionInfo(QuestionVO questionVO, Model model) {
		// 문의 단건정보
		QuestionVO findQuestionVO = questionService.selectQuestionInfo(questionVO);
		// 답변 전체조회
		List<AnswerVO> findAnswerVO = answerService.selectAnswerAll(questionVO);
		
		// 문의유형 분기처리
		String getQT = findQuestionVO.getQuestionType();
		switch (getQT) {
		case "MA1" : findQuestionVO.setQuestionType("가계부");
		break;
		case "MA2" : findQuestionVO.setQuestionType("중고거래");
		break;
		case "MA3" : findQuestionVO.setQuestionType("알뜰모임");
		break;
		case "MA4" : findQuestionVO.setQuestionType("커뮤니티");
		break;
		case "MA5" : findQuestionVO.setQuestionType("메모");
		break;
		case "MA6" : findQuestionVO.setQuestionType("홀로페이");
		break;
		case "MA7" : findQuestionVO.setQuestionType("포인트");
		break;
		case "MA8" : findQuestionVO.setQuestionType("기타");
		break;
		default:
			System.out.println("");
		}
		
		model.addAttribute("questionInfo", findQuestionVO);
		model.addAttribute("answerInfo", findAnswerVO);
		return "admin/question/questionDetail";
	}

}
