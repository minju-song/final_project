package com.holoyolo.app.answer.web;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.holoyolo.app.answer.service.AnswerService;
import com.holoyolo.app.answer.service.AnswerVO;


@Controller
public class AnswerController {
	
	@Autowired
	AnswerService answerService;
	

	
	// 등록
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
	
	// 수정
	@PostMapping("/admin/question/detail/update/{questionId}/{answerId}")
	@ResponseBody
	public Map<String, Object> answerUpdate(@PathVariable ("answerId") int answerId, 
			                                @PathVariable("questionId") int questionId,
			                                @RequestBody AnswerVO answerVO) {
		System.out.println(answerVO);
		answerVO.setQuestionId(questionId);
		answerVO.setAnswerId(answerId);
		answerVO.setAnswerContent(answerVO.getAnswerContent());
//		Map<String, Object> result = answerService.updateAnswerInfo(answerVO);
//		System.out.println(result);
	
		
		return answerService.updateAnswerInfo(answerVO);
	}

	// 삭제
	@DeleteMapping("/admin/question/detail/delete/{questionId}/{answerId}")
	@ResponseBody
	public boolean answerDelete(@PathVariable ("answerId") int answerId, @PathVariable("questionId") int questionId) {
		return answerService.deleteAnswerInfo(answerId);
	}
}
