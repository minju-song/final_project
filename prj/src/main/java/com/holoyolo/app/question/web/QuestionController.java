package com.holoyolo.app.question.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
		List<QuestionVO> list = questionService.selectQuestionAll();
		model.addAttribute("questionList", list);
		return "admin/question/questionPage";
	}
	
	// 문의 조건조회
    @GetMapping("/admin/question/totalList")
    public String getTotalList(Model model) {
        List<QuestionVO> totalList = questionService.selectQuestionTotalList();
        model.addAttribute("totalList", totalList);
        return "admin/question";
    }

    @GetMapping("/admin/question/pendingList")
    public String getPendingList(Model model) {
        return "admin/question";
    }

    @GetMapping("/admin/question/completedList")
    public String getCompletedList(Model model) {
        return "admin/question";
    }

	// 문의 단건조회
	@GetMapping("/admin/question/detail")
	public String selectQuestionInfo(QuestionVO questionVO, Model model) {
		Map<String, Object> questionInfo = questionService.selectQuestionInfo(questionVO);
		model.addAttribute("questionInfo", questionInfo.get("questionInfo"));
		model.addAttribute("answerInfo", questionInfo.get("answerInfo"));
		return "admin/question/questionDetail";
	}

	// 문의 개수조회
    @RequestMapping("/admin/question/count")
    @ResponseBody
    public Map<String, Integer> getQuestionCountsAjax() {
        Map<String, Integer> questionCounts = new HashMap<>();
        System.out.println(questionService.selectQuestionTotalCount());
        questionCounts.put("totalQuestionCount", questionService.selectQuestionTotalCount());
        questionCounts.put("pendingQuestionCount", questionService.selectQuestionPendingCount());
        questionCounts.put("completedQuestionCount", questionService.selectQuestionCompletedCount());

        return questionCounts;
    }

}
