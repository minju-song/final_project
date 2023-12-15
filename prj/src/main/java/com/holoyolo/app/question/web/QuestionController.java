package com.holoyolo.app.question.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.holoyolo.app.attachment.service.AttachmentService;
import com.holoyolo.app.attachment.service.AttachmentVO;
import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.question.service.QuestionService;
import com.holoyolo.app.question.service.QuestionVO;

@Controller
public class QuestionController {

	@Autowired
	QuestionService questionService;

	@Autowired
	AttachmentService attachmentService;

	// 문의 전체조회
	@GetMapping("/admin/question")
	public String selectQuestionList(Model model) {
		return "admin/question/questionPage";
	}

	// 문의 조건 조회
	@GetMapping("/admin/question/list")
	@ResponseBody
	public Map<String, Object> getQuestionListAjax(QuestionVO questionVO) {
		// 문의정보 가져오기
		Map<String, Object> questionMap = new HashMap<>();

		questionMap.put("questionYn", questionVO.getQuestionYn());
		questionMap.put("list", questionService.selectQuestionTotalList(questionVO));
		questionMap.put("count", questionService.selectQuestionTotalCount(questionVO));
		return questionMap;
	}

	// 문의 단건조회
	@GetMapping("/admin/question/detail")
	public String selectQuestionInfo(QuestionVO questionVO, Model model) {

		// 문의단건 가져오기
		Map<String, Object> questionInfo = questionService.selectQuestionInfo(questionVO);

		model.addAttribute("imgInfo", questionInfo.get("imgInfo"));
		model.addAttribute("attachInfo", questionInfo.get("attachInfo"));
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
	public String boardList(@AuthenticationPrincipal PrincipalDetails principalDetails, Model mo,
			@PageableDefault(size = 10) Pageable pageable) {
		String memberId = (String) principalDetails.getUsername();

		// 서비스를 통해 질문 목록 가져오기
		Page<QuestionVO> list = questionService.MyQuestionList(memberId, pageable);

		// 뷰에 결과 전달
		mo.addAttribute("qnaList", list);
//mo.addAttribute("page",list);
		mo.addAttribute("menu", "cs");
		return "user/cs/questionList";
	}

	// 문의 등록 페이지
	@GetMapping("/member/cs/help/question/insert")
	public String insertQuestionPage(Model mo, QuestionVO questionVO) {
		mo.addAttribute("questionVO", questionVO);
		mo.addAttribute("boardType", "1:1문의");
		mo.addAttribute("menu", "cs");
		return "user/cs/questionInsert";
	}

	// 등록
	@PostMapping("/question/insert")
	public String noticeInsertProc(@AuthenticationPrincipal PrincipalDetails principalDetails, QuestionVO questionVO,
			@RequestParam("imageFiles") MultipartFile[] imageFiles,
			@RequestParam("attachmentFiles") MultipartFile[] attachmentFiles,Model mo) {
		
		System.out.println(questionVO);
		List<AttachmentVO> imgList = attachmentService.uploadFiles(imageFiles, "questionImg");
		List<AttachmentVO> attachList = attachmentService.uploadFiles(attachmentFiles, "questionAttach");
		questionVO.setMemberId(principalDetails.getUsername());
		questionService.insertQuestion(questionVO, imgList, attachList);
		mo.addAttribute("boardType","1:1 문의");
		return "redirect:/member/cs/help/question";
//		return "/question/insert";
	}

	// 회원 문의 상세보기
	@GetMapping("/member/cs/help/qna/view")
	public String boardInfo(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@RequestParam(name = "id") int questionId, Model mo) {
		AttachmentVO attachmentVO = new AttachmentVO();
		QuestionVO questionVO = new QuestionVO();
		questionVO.setQuestionId(questionId);
		String loginId = "not found";
		if (principalDetails != null) {
			loginId = principalDetails.getUsername();
		}

		Map<String, Object> vo = questionService.selectQuestionInfo(questionVO);
		attachmentVO.setPostId(questionVO.getQuestionId());
		attachmentVO.setMenuType("AA8");
		Map<String, List<AttachmentVO>> returnMap = attachmentService.getCSAttachmentList(attachmentVO);
		mo.addAttribute("menu", "cs");
		mo.addAttribute("questionVO", vo.get("questionInfo"));
		mo.addAttribute("loginId", loginId);
		mo.addAttribute("questionImg", returnMap.get("imgList"));
		mo.addAttribute("questionAttach", returnMap.get("attachList"));

		return "user/cs/questionView";

	}

	@PostMapping("/deleteQNA")
	@ResponseBody
	public Map<String, Object> deletequestion(@RequestBody QuestionVO reqquestionVO) {

		// 본문삭제
		boolean questionResult = questionService.deleteQuestionInfo(reqquestionVO.getQuestionId());
		// 첨부및 이미지 삭제

		int attResult = attachmentService.deleteQNAAttachment("AA8", reqquestionVO.getQuestionId());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (questionResult || attResult == 1) {
			resultMap.put("resultMsg", "공지가 삭제되었습니다");
			resultMap.put("resultCode", "1");
		} else {
			resultMap.put("resultMsg", "err");
			resultMap.put("resultCode", "0");
		}
		return resultMap;
	}

	@GetMapping("/cs/help/question/update")
	public String updateView(@AuthenticationPrincipal PrincipalDetails principalDetails, AttachmentVO attachmentVO,
			@RequestParam(name = "boardId") int questionId, Model mo, QuestionVO questionVO) {
		String loginId = "not found";
		if (principalDetails != null) {
			loginId = principalDetails.getUsername();
		}
		attachmentVO.setPostId(questionId);
		attachmentVO.setMenuType("AA8");
		Map<String, List<AttachmentVO>> returnMap = attachmentService.getCSAttachmentList(attachmentVO);
		questionVO = questionService.selectQuestion(questionId);
		mo.addAttribute("loginId", loginId);
		mo.addAttribute("menu", "cs");
		mo.addAttribute("questionVO", questionVO);
		mo.addAttribute("questionImg", returnMap.get("imgList"));
		mo.addAttribute("questionAttach", returnMap.get("attachList"));
		System.out.println("==================" + returnMap);
		return "user/cs/questionUpdate";
	}

	// 수정등록
	@PostMapping("/question/update")
	public String noticeUpdateProcess(@AuthenticationPrincipal PrincipalDetails principalDetails, QuestionVO questionVO,
			@RequestParam("imageFiles") MultipartFile[] imageFiles,
			@RequestParam("attachmentFiles") MultipartFile[] attachmentFiles) {

		List<AttachmentVO> imgList = attachmentService.uploadFiles(imageFiles, "questionImg");
		List<AttachmentVO> attachList = attachmentService.uploadFiles(attachmentFiles, "questionAttach");
		questionVO.setMemberId(principalDetails.getUsername());
		questionService.updateQuestion(questionVO, imgList, attachList);
		return "redirect:/member/cs/help/question";
	}

}
