package com.holoyolo.app.memo.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.holoyolo.app.attachment.service.AttachmentService;
import com.holoyolo.app.attachment.service.AttachmentVO;
import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.memo.service.MemoService;
import com.holoyolo.app.memo.service.MemoVO;

@Controller
public class MemoController {
	
	@Autowired
	MemoService memoService;
	@Autowired
	AttachmentService attachmentService;
	
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
	
	//단건조회
	@GetMapping("member/memoInfo")
	@ResponseBody
	public MemoVO memoInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, 
						   MemoVO memoVO) {
		memoVO.setMemberId(principalDetails.getUsername());
		return memoService.getMemo(memoVO);
	}
	
	//등록
	@PostMapping("member/memoInsert")
	@ResponseBody
	public MemoVO memoInsert(@AuthenticationPrincipal PrincipalDetails principalDetails, 
						  MemoVO memoVO,
						  MultipartFile[] uploadFiles) {
		System.out.println("들어옴");
		List<AttachmentVO> imgList = attachmentService.uploadFiles(uploadFiles, "memo");
		memoVO.setMemberId(principalDetails.getUsername());
		int id = memoService.insertMemo(memoVO, imgList);
		MemoVO memoInfo = new MemoVO();
		memoInfo.setMemoId(id);
		memoInfo.setMemberId(principalDetails.getUsername());
		memoInfo = memoService.getMemo(memoInfo);
		return memoInfo;
	}
	
	//수정
	@PostMapping("member/memoUpdate")
	@ResponseBody
	public void memoUpdate(@AuthenticationPrincipal PrincipalDetails principalDetails, 
						   MemoVO memoVO) {
		memoVO.setMemberId(principalDetails.getUsername());
		memoService.updateMemo(memoVO);
	}
	
	//삭제
	@GetMapping("member/memoDelete")
	@ResponseBody
	public void deleteMemo(@AuthenticationPrincipal PrincipalDetails principalDetails,
						   MemoVO memoVO){
		memoVO.setMemberId(principalDetails.getUsername());
		memoService.deleteMemo(memoVO);
	}
	
	//index 수정
	@PostMapping("member/updateIndex")
	@ResponseBody
	public void memoIndex(@AuthenticationPrincipal PrincipalDetails principalDetails, 
						  MemoVO memoVO){
		memoVO.setMemberId(principalDetails.getUsername());
		memoService.memoIndex(memoVO);
	}
	
	@PostMapping("member/memoUploadImg")
	@ResponseBody
	public String memoUploadImg(MemoVO memoVO, @RequestPart MultipartFile[] uploadFiles) {
		System.out.println("memoVO ::::: " + memoVO);
		List<AttachmentVO> imgList = attachmentService.uploadFiles(uploadFiles, "memo");
		int result = memoService.memoUploadImg(memoVO, imgList);
		if(result > 0) {
			return "Success";
		}
		return "Fail";
	}
}
