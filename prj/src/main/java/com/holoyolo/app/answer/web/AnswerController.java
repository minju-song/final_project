package com.holoyolo.app.answer.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.holoyolo.app.answer.service.AnswerService;
import com.holoyolo.app.answer.service.AnswerVO;

/*
 *  작성자 : 공성훈
 *  작업 : 답변 CRUD
 *  언제 왜 고쳤는지
 */

@Controller
public class AnswerController {
	
	@Autowired
	AnswerService answerService;
	

	// 등록
	@PostMapping("/admin/question/detail")
	public String answerInsertProcess(AnswerVO answerVO, 
									  RedirectAttributes ratt) {
		int answerId = answerService.insertAnswerInfo(answerVO);
		String msg = "";
		if(answerId == -1) { 
			msg = "답변이 등록되지 않았습니다.";
		} else {
			msg = "답변이 등록 되었습니다.";
		}
		ratt.addFlashAttribute("result", msg);
		ratt.addAttribute("questionId", answerVO.getQuestionId());
		// flash 휘발성
		return "redirect:/admin/question/detail";
	}
	
	// 수정
	@PutMapping("/admin/question/detail/{questionId}/{answerId}")
	@ResponseBody
	public Map<String, Object> answerUpdate(@PathVariable ("answerId") int answerId, 
			                                @PathVariable("questionId") int questionId,
			                                @RequestBody AnswerVO answerVO) {
		answerVO.setQuestionId(questionId);
		answerVO.setAnswerId(answerId);
		return answerService.updateAnswerInfo(answerVO);
	}

	// 삭제
	@DeleteMapping("/admin/question/detail/{questionId}/{answerId}")
	@ResponseBody
	public boolean answerDelete(@PathVariable ("answerId") int answerId, 
								@PathVariable("questionId") int questionId) {
		return answerService.deleteAnswerInfo(answerId);
	}
}
