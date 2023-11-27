package com.holoyolo.app.answer.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.holoyolo.app.answer.service.AnswerService;
import com.holoyolo.app.answer.service.AnswerVO;


@Controller
public class AnswerController {
	
	@Autowired
	AnswerService answerService;
	

	
	// 답변 등록
	@PostMapping("/admin/question/detail")
	public String answerInsertProcess(@RequestParam("questionId") int questionId, @RequestParam("answerContent") String answerContent, AnswerVO answerVO, RedirectAttributes ratt) {
	    if (answerService == null) {
	        // answerService가 null인 경우, 로깅 또는 예외 처리를 추가할 수 있습니다.
	        return "redirect:/error";
	    }
		answerVO.setQuestionId(questionId);
		answerVO.setAnswerContent(answerContent);
		int answerId = answerService.insertAnswerInfo(answerVO);
		String msg = null;
		if(answerId > -1) {
			msg = "";
		}
		else {
			msg = "답변이 등록되지 않았습니다.";
		}
		ratt.addFlashAttribute("result", msg);
		
		return "redirect:/admin/question/detail?questionId="+ questionId;	
	}
    
	// 답변 삭제
	@DeleteMapping("/admin/question/detail/{questionId}/answer/{answerId}")
	public String answerDeleteInfo(@RequestParam int answerId, @RequestParam("questionId") int questionId, RedirectAttributes ratt) {
		System.out.println(answerId);
		System.out.println(questionId);
		
		boolean result = answerService.deleteAnswerInfo(answerId);
		
		String msg = null;
		if (result) {
			msg = "정상적으로 삭제되었습니다.\n삭제대상: " + answerId;
		} else {
			msg = "정상적으로 삭제되지 않았습니다.\n정보를 확인해주시기바랍니다.\n삭제요청: " + answerId;
		}
		ratt.addFlashAttribute("message", msg);
		return "redirect:/admin/question/detail?questionId="+ questionId;
	}
}
