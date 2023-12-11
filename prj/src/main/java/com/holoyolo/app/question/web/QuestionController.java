package com.holoyolo.app.question.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
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

	@GetMapping("/cs/faq")
	public String fapPage(Model model) {
		// 사이드메뉴 정보 넘기기
		model.addAttribute("menu", "cs");

		return "user/cs/faq";
	}

	
	 @GetMapping("/member/cs/help/question")
	    public String boardList(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model, @PageableDefault(page=0, size=10, sort="writeDate", direction=Sort.Direction.DESC) Pageable pageable){


	        Page<QuestionVO> list = questionService.MyQuestionList((String)principalDetails.getUsername(), pageable);


	        //페이지블럭 처리
	        //1을 더해주는 이유는 pageable은 0부터라 1을 처리하려면 1을 더해서 시작해주어야 한다.
	        int nowPage = list.getPageable().getPageNumber() + 1;
	        //-1값이 들어가는 것을 막기 위해서 max값으로 두 개의 값을 넣고 더 큰 값을 넣어주게 된다.
	        int startPage =  Math.max(nowPage - 4, 1);
	        int endPage = Math.min(nowPage+9, list.getTotalPages());


	        model.addAttribute("list", list);
	        model.addAttribute("nowPage",nowPage);
	        model.addAttribute("startPage", startPage);
	        model.addAttribute("endPage", endPage);



	        return "boardlist";
	    }
	
}
