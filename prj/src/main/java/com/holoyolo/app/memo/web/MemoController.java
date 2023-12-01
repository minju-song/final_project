package com.holoyolo.app.memo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.memo.service.MemoService;
import com.holoyolo.app.memo.service.MemoVO;

@Controller
public class MemoController {
	
	@Autowired
	MemoService memoService;
	
	//전체조회
	@GetMapping("member/memoList")
	public String memoList(@AuthenticationPrincipal PrincipalDetails principalDetails, 
			               MemoVO memoVO, 
			               Model model) {
		memoVO.setMemberId(principalDetails.getUsername());
		List<MemoVO> list = memoService.getMemoList(memoVO);
		model.addAttribute("memoList", list);
		
		return "user/memo/memoList";
	}
	
	
	//등록
	@PostMapping("member/memoInsert")
	@ResponseBody
	public int memoInsert(@AuthenticationPrincipal PrincipalDetails principalDetails, MemoVO memoVO) {
		memoVO.setMemberId(principalDetails.getUsername());
		int id = memoService.insertMemo(memoVO);
		return id;
	}
	
	//수정
	@PostMapping("member/memoUpdate")
	@ResponseBody
	public void memoUpdate(@AuthenticationPrincipal PrincipalDetails principalDetails, MemoVO memoVO) {
		memoVO.setMemberId(principalDetails.getUsername());
		memoService.updateMemo(memoVO);
	}
	
	//삭제
	@GetMapping("member/memoDelete")
	@ResponseBody
	public void deleteMemo(@AuthenticationPrincipal PrincipalDetails principalDetails,MemoVO memoVO){
		memoVO.setMemberId(principalDetails.getUsername());
		memoService.deleteMemo(memoVO);
	}
	
	//index 수정
	@PostMapping("member/updateIndex")
	@ResponseBody
	public void memoIndex(@AuthenticationPrincipal PrincipalDetails principalDetails, MemoVO memoVO){
		memoVO.setMemberId(principalDetails.getUsername());
		memoService.memoIndex(memoVO);
	}
}
