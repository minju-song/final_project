package com.holoyolo.app.memo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.holoyolo.app.memo.service.MemoService;
import com.holoyolo.app.memo.service.MemoVO;

@Controller
public class MemoController {
	
	@Autowired
	MemoService memoService;
	
	//전체조회
	@GetMapping("member/memoList")
	public String memoList(MemoVO memoVO, Model model) {
		memoVO.setMemberId("sumin@mail.com");
		List<MemoVO> list = memoService.getMemoList(memoVO);
		System.out.println(list);
		model.addAttribute("memoList", list);
		return "member/memoList";
	}
	
	//단건조회
	@GetMapping("member/memoInfo")
	public String memoInfo(MemoVO memoVO, Model model) {
		model.addAttribute("memoInfo", memoService.getMemo(memoVO));
		return "memoInfo";
	}
	
	//등록
	
	
	//수정
	
	
	//삭제
	
}
