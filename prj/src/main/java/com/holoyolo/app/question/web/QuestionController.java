package com.holoyolo.app.question.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.question.service.QuestionService;
import com.holoyolo.app.question.service.QuestionVO;

@Controller
public class QuestionController {

	@Autowired
	QuestionService questionService;

	// 문의 전체조회
	@GetMapping("/admin/question")
	public String selectQuestionList(Model model) {
		return "admin/question/questionPage";
	}

	// 문의 조건 조회
	@GetMapping("/admin/question/list")
	@ResponseBody
	public Map<String, Object> getQuestionListAjax(QuestionVO questionVO) {
		Map<String, Object> questionMap = new HashMap<>();
		questionMap.put("questionYn", questionVO.getQuestionYn());
		questionMap.put("list", questionService.selectQuestionTotalList(questionVO));
		questionMap.put("count", questionService.selectQuestionTotalCount(questionVO));
		return questionMap;
	}

	// 문의 단건조회
	@GetMapping("/admin/question/detail")
	public String selectQuestionInfo(QuestionVO questionVO, Model model) {
		Map<String, Object> questionInfo = questionService.selectQuestionInfo(questionVO);
		model.addAttribute("questionInfo", questionInfo.get("questionInfo"));
		model.addAttribute("answerInfo", questionInfo.get("answerInfo"));
		return "admin/question/questionDetail";
	}

}
