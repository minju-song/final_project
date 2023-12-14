package com.holoyolo.app.trade.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.holoyolo.app.holopayHistory.service.HoloPayHistoryService;
import com.holoyolo.app.member.service.MemberVO;
import com.holoyolo.app.pointHistory.service.PointHistoryService;
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
	
	@Autowired
	HoloPayHistoryService holoPayService;
	
	@Autowired
	PointHistoryService pointService;

	@GetMapping("/admin/trade")
	public String selectTradeList(Model model) {
		return "admin/tradeMgt";
	}
	
	//중고거래 페이지 이동
	@GetMapping("/tradeList")
	public String tradeList() {
		return "user/trade/tradeList";
	}
	
	//중고거래 페이징
	@GetMapping("tradePaging")
	@ResponseBody
	public Map<String, Object> tradePaging(@AuthenticationPrincipal PrincipalDetails principalDetails,
										   TradeVO tradeVO){
		Map<String, Object> map = tradeService.getTradeList(tradeVO);
		if (principalDetails != null) {
            map.put("memberId", principalDetails.getUsername());
        }
		return map;
	}
	
	//등록페이지 이동
	@GetMapping("member/tradeInsert")
	public String tradeInsert(TradeVO tradeVO) {
		return "user/trade/tradeInsert";
	}
	
	//등록
	@PostMapping("member/tradeInsert")
	public String tradeInsertProcess(@AuthenticationPrincipal PrincipalDetails principalDetails, 
									TradeVO tradeVO,
									@RequestPart MultipartFile[] uploadFiles) {
		List<AttachmentVO> imgList = attachmentService.uploadFiles(uploadFiles, "trade");
		tradeVO.setSellerId(principalDetails.getUsername());
		tradeService.insertTrade(tradeVO, imgList);
		return "redirect:/tradeList";
	}
	
	//상세보기
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
		attachmentVO.setMenuType("AA1");
		model.addAttribute("tradeImg", attachmentService.getAttachmentList(attachmentVO));
		model.addAttribute("heartCount", heartService.getHeartCount(heartVO));
		return "user/trade/tradeInfo";
	}
	
	//수정페이지 이동
	@GetMapping("member/tradeUpdate")
	public String tradeUpdate(@AuthenticationPrincipal PrincipalDetails principalDetails,
							  Model model,
							  AttachmentVO attachmentVO,
							  TradeVO tradeVO) {
		tradeVO.setSellerId(principalDetails.getUsername());
		model.addAttribute("tradeInfo", tradeService.getTrade(tradeVO));
		attachmentVO.setPostId(tradeVO.getTradeId());
		attachmentVO.setMenuType("AA1");
		model.addAttribute("tradeImg", attachmentService.getAttachmentList(attachmentVO));
		System.out.println(attachmentService.getAttachmentList(attachmentVO));
		return "user/trade/tradeUpdate";
	}
	
	//수정
	@PostMapping("member/tradeUpdate")
	public String tradeUpdateProcess(@AuthenticationPrincipal PrincipalDetails principalDetails, 
									TradeVO tradeVO,
									@RequestPart MultipartFile[] uploadFiles) {
		List<AttachmentVO> imgList = attachmentService.uploadFiles(uploadFiles, "trade");
		tradeVO.setSellerId(principalDetails.getUsername());
		tradeService.updateTradeImg(tradeVO, imgList);
		tradeService.updateTrade(tradeVO);
		return "redirect:/tradeList";
	}
	
	//구매자, 거래상태 수정
	@PostMapping("member/BuyerIdUpdate")
	@ResponseBody
	public String buyerIdUpdate(@AuthenticationPrincipal PrincipalDetails principalDetails, 
								TradeVO tradeVO) {
		if(tradeVO.getPromiseStatus().equals("")) {
			tradeVO.setBuyerId(null);
		}else if(tradeVO.getPromiseStatus().equals("TD1")) {
			tradeVO.setBuyerId(principalDetails.getUsername());
		}
		tradeService.updateBuyerId(tradeVO);
		return "redirect:/tradeList";
	}
	
	//구매자, 거래상태 수정
	@GetMapping("member/BuyerIdUpdate")
	public String buyerIdUpdateMail(TradeVO tradeVO) {
		tradeService.updateBuyerId(tradeVO);
		return "member/";
	}
	
	//삭제
	@GetMapping("member/tradeDelete")
	@ResponseBody
	public void tradeDelete(@AuthenticationPrincipal PrincipalDetails principalDetails,
						   TradeVO tradeVO,
						   AttachmentVO attachmentVO){
		tradeVO.setSellerId(principalDetails.getUsername());
		tradeService.deleteTrade(tradeVO);
		attachmentVO.setPostId(tradeVO.getTradeId());
		attachmentVO.setMenuType("AA1");
		attachmentService.deleteAttachment(attachmentVO);
	}
	
	//계산페이지 이동
	@GetMapping("member/tradePay")
	public String tradePay(@AuthenticationPrincipal PrincipalDetails principalDetails,
						   HttpServletRequest request,
						   TradeVO tradeVO,
						   MemberVO memberVO,
						   Model model) {
		memberVO.setMemberId(principalDetails.getUsername());
		model.addAttribute("price", Integer.parseInt(request.getParameter("price")));
		model.addAttribute("sellerId", request.getParameter("sellerId"));
		model.addAttribute("tradeId", request.getParameter("tradeId"));
		System.out.println(request.getParameter("sellerId"));
		model.addAttribute("holoPayCnt", holoPayService.holopayBalance(memberVO));
		model.addAttribute("pointCnt", pointService.pointBalance(memberVO));
		return "user/trade/tradePay";
	}
	
	//삭제
	@GetMapping("member/attachmentDelete")
	@ResponseBody
	public void attachmentDelete(@AuthenticationPrincipal PrincipalDetails principalDetails,
								 AttachmentVO attachmentVO){
		attachmentService.deleteAttachment(attachmentVO);
	}
	
	//포인트, 홀로페이 등록(구매자)
	@PostMapping("member/insertPayPoint")
	@ResponseBody
	public void insertPayPoint(@AuthenticationPrincipal PrincipalDetails principalDetails, 
							   MemberVO memberVO,
							   TradeVO tradeVO) {
		memberVO.setMemberId(principalDetails.getUsername());
		System.out.println(memberVO);
		tradeService.insertPayPoint(memberVO);
		tradeVO.setTradeId(tradeVO.getTradeId());
	}
	
	//포인트, 홀로페이 등록(판매자)
		@PostMapping("member/insertPayPointSeller")
		@ResponseBody
		public void insertPayPointSeller(MemberVO memberVO,
								   		 TradeVO tradeVO) {
			System.out.println(memberVO);
			tradeService.insertPayPointSeller(memberVO);
			tradeVO.setPromiseStatus("TD3");
			tradeVO.setTradeId(tradeVO.getTradeId());
			tradeService.updateBuyerId(tradeVO);
		}
	
	//마이페이지 나의 알뜰모임 페이지
	@GetMapping("member/myTrade")
	public String myTradePage(@AuthenticationPrincipal PrincipalDetails principalDetails, 
							Model model,
							TradeVO tradeVO) {
		String page = "";
		if(tradeVO.getListType().equals("HLIST")) {
			tradeVO.setMemberId(principalDetails.getUsername());
			page = "user/mypage/myHeart";
		}else if(tradeVO.getListType().equals("BLIST")){
			tradeVO.setBuyerId(principalDetails.getUsername());
			page = "user/mypage/myBuy";
		}else {
			tradeVO.setSellerId(principalDetails.getUsername());
			page = "user/mypage/mySell";
		}
		List<TradeVO> list = tradeService.selectMyTradeList(tradeVO);
		
		
		for(int i=0; i<list.size(); i++) {
			String str = list.get(i).getTradePlace();
			if(str != null) {
				String[] newPlace = str.split(" ");
				String result = newPlace[0] + " " + newPlace[1];
				list.get(i).setTradePlace(result);
			}
		}
		
		//String[] place = str.split(",");
		model.addAttribute("menu", "mypage");
		model.addAttribute("tradeList", list);
		return page;
	}

}
