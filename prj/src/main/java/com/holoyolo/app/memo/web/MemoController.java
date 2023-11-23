package com.holoyolo.app.memo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		/* System.out.println(list); */
		model.addAttribute("memoList", list);
		return "user/memo/memoList";
	}
	
	//단건조회
	@GetMapping("member/memoInfo")
	public String memoInfo(MemoVO memoVO, Model model) {
		model.addAttribute("memoInfo", memoService.getMemo(memoVO));
		return "redirect:user/memo/memoList";
	}
	
	//등록
	@PostMapping("member/memoInsert")
	public String memoInsert(MemoVO memoVO) {
		memoService.insertMemo(memoVO);
		return "redirect:user/memo/memoList";
	}
	
	//수정
	
	
	//삭제
	@GetMapping("member/memoDelete")
	public String deleteMemo(@RequestParam Integer memoId, @RequestParam String memberId, RedirectAttributes ratt) {
		int result = memoService.deleteMemo(memoId, memberId);
		String msg = null;
		if(result > -1) {
			msg = "메모가 삭제되었습니다.";
		}else {
			msg = "메모 삭제에 실패하였습니다.";
		}
		ratt.addFlashAttribute("result", msg);
		return "redirect:user/memo/memoList";
	}
}
