package com.holoyolo.app.trade.web;

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
import com.holoyolo.app.heart.service.HeartService;
import com.holoyolo.app.heart.service.HeartVO;
import com.holoyolo.app.trade.service.TradeService;
import com.holoyolo.app.trade.service.TradeVO;

@Controller
public class TradeController {
	@Autowired
	TradeService tradeService;
	
	@Autowired
	AttachmentService attachmentService;
	
	@Autowired
	HeartService heartService;

	@GetMapping("/admin/trade")
	public String selectTradeList(Model model) {
		List<TradeVO> list = tradeService.getTradeList();
		model.addAttribute("tradeList", list);
		return "admin/tradeMgt";
	}
	
	//중고거래 페이지 이동
	@GetMapping("/tradeList")
	public String tradeList(Model model) {
		return "user/trade/tradeList";
	}
	
	//중고거래 페이징
	@GetMapping("tradePaging")
	@ResponseBody
	public Map<String, Object> tradePaging(TradeVO tradeVO){
		Map<String, Object> map = tradeService.tradePaging(tradeVO);
		return map;
	}
	
	@GetMapping("member/tradeInsert")
	public String tradeInsert(TradeVO tradeVO) {
		return "user/trade/tradeInsert";
	}
	
	@PostMapping("member/tradeInsert")
	public String tradeInsertProcess(@AuthenticationPrincipal PrincipalDetails principalDetails, 
									TradeVO tradeVO,
									@RequestPart MultipartFile[] uploadFiles) {
		List<AttachmentVO> imgList = attachmentService.uploadFiles(uploadFiles, "trade");
		tradeVO.setSellerId(principalDetails.getUsername());
		tradeService.insertTrade(tradeVO, imgList);
		return "redirect:/tradeList";
	}
	
	@GetMapping("member/tradeInfo")
	public String tradeInfo(@AuthenticationPrincipal PrincipalDetails principalDetails,
							TradeVO tradeVO, 
							Model model,
							AttachmentVO attachmentVO,
							HeartVO heartVO) {
		attachmentVO.setPostId(tradeVO.getTradeId());
		heartVO.setTradeId(tradeVO.getTradeId());
		tradeService.updateViews(tradeVO);
		model.addAttribute("memberId", principalDetails.getUsername());
		heartVO.setMemberId(principalDetails.getUsername());
		model.addAttribute("heartInfo", heartService.getHeart(heartVO));
		model.addAttribute("tradeInfo", tradeService.getTrade(tradeVO));
		model.addAttribute("tradeImg", attachmentService.getAttachmentList(attachmentVO));
		model.addAttribute("heartCount", heartService.getHeartCount(heartVO));
		return "user/trade/tradeInfo";
	}
	
	@GetMapping("member/tradeUpdate")
	public String tradeUpdate(@AuthenticationPrincipal PrincipalDetails principalDetails,
							  Model model,
							  AttachmentVO attachmentVO,
							  TradeVO tradeVO) {
		tradeVO.setSellerId(principalDetails.getUsername());
		model.addAttribute("tradeInfo", tradeService.getTrade(tradeVO));
		System.out.println(tradeService.getTrade(tradeVO));
		model.addAttribute("tradeImg", attachmentService.getAttachmentList(attachmentVO));
		return "user/trade/tradeUpdate";
	}
	
	@PostMapping("member/tradeUpdate")
	public String tradeUpdateProcess(@AuthenticationPrincipal PrincipalDetails principalDetails, 
									TradeVO tradeVO,
									@RequestPart MultipartFile[] uploadFiles) {
		List<AttachmentVO> imgList = attachmentService.uploadFiles(uploadFiles, "trade");
		tradeVO.setSellerId(principalDetails.getUsername());
		tradeService.insertTrade(tradeVO, imgList);
		return "redirect:/tradeList";
	}
	
	@GetMapping("member/tradeDelete")
	@ResponseBody
	public void tradeDelete(@AuthenticationPrincipal PrincipalDetails principalDetails,
						   TradeVO tradeVO){
		tradeVO.setSellerId(principalDetails.getUsername());
		tradeService.deleteTrade(tradeVO);
	}
	

}
