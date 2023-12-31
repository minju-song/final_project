package com.holoyolo.app.chat.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.chat.service.club.ChatRoomVO;
import com.holoyolo.app.chat.service.club.ChatService;
import com.holoyolo.app.chat.service.club.ChatVO;
import com.holoyolo.app.chat.service.trade.TradeChatRoomVO;
import com.holoyolo.app.chat.service.trade.TradeChatService;
import com.holoyolo.app.chat.service.trade.TradeChatVO;
import com.holoyolo.app.club.service.ClubService;
import com.holoyolo.app.club.service.ClubVO;
import com.holoyolo.app.clubMember.service.ClubMemberService;
import com.holoyolo.app.clubMember.service.ClubMemberVO;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;
import com.holoyolo.app.trade.service.TradeService;
import com.holoyolo.app.trade.service.TradeVO;

@Controller
public class ChatController {

	@Autowired
	ClubService clubService;
	
	@Autowired
	ChatService chatService;

	@Autowired
	ClubMemberService clubMemberService;
	
	@Autowired
	TradeService tradeService;
	
	@Autowired
	TradeChatService tradeChatService;
	
	@Autowired
	MemberService memberService;
	
	
	@GetMapping("/member/club/clubChat")
	public String clubChatting(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model, HttpSession session, ClubVO vo) {
		Map<String, Object> map = new HashMap<>();
		
		//클럽정보
		vo = clubService.getClub(vo.getClubId());
		map.put("club", vo);
		System.out.println("채팅방입장");
		
		//채팅방
		ChatRoomVO room = chatService.findOrCreateRoom(vo.getClubId());

		//모임멤버여부
		ClubMemberVO cmvo = new ClubMemberVO();
		cmvo.setClubId(vo.getClubId());
		cmvo.setMemberId(principalDetails.getUsername());
		cmvo = clubMemberService.checkMyClub(cmvo);
		if(cmvo != null) {
			if(cmvo.getStopDate() != null) {
				if(cmvo.getJoinDate() == null) {
					session.setAttribute("check", "재가입승인대기");
				}
				else {					
					session.setAttribute("check", "탈퇴");
				}
			}
			else if (cmvo.getJoinDate() == null) {
				session.setAttribute("check", "승인대기");
			}
			else {				
				session.setAttribute("check", true);
			}
		}
		else {
			session.setAttribute("check", false);
		}
		
		
		System.out.println(room.getClubId());
		System.out.println(principalDetails.getMemberVO().getMemberName());
		
		model.addAttribute("room", room);
		model.addAttribute("member", principalDetails.getMemberVO());
		model.addAttribute("result", map);
		model.addAttribute("menu", "club");
		model.addAttribute("subMenu", "chat");
		
		
		return "user/club/clubChat";
	}
	
	//공지사항저장
	@PostMapping("/member/insertChat")
	@ResponseBody
	public Map<String, Object> insertChat(@RequestBody String str){
		Map<String, Object> map = new HashMap<>();
		System.out.println("db저장값 : "+str);

		if(chatService.insertChat(str) > 0) {
			map.put("result", "success");
		}
		else {
			map.put("result", "fail");
		}
		return map;
	}
	
	//가장 최근 공지사항 가져오기
	@GetMapping("/getLatestNotice")
	@ResponseBody
	public String getLatestNotice(ClubVO vo) {

		return chatService.getLatestNotice(vo.getClubId());
	}
	
	//공지사항리스트가져오기
	@GetMapping("/getNoticeList")
	@ResponseBody
	public Map<String, Object> getNoticeList(ClubVO vo) {
		return chatService.getNoticeList(vo.getClubId());
	}
	
	//중고거래 채팅
	@GetMapping("member/tradeChat")
	public String tradeChat(@AuthenticationPrincipal PrincipalDetails principalDetails,
							Model model, 
							TradeVO tradeVO) {
		Map<String, Object> map = new HashMap<>();
		map = tradeChatService.tradeChatPage(tradeVO);


		//중고거래 조회
		tradeVO = tradeService.selectTrade2(tradeVO);
		map.put("trade", tradeVO);
		
		TradeChatRoomVO room = tradeChatService.findOrCreateRoom(tradeVO);
		String youId;
		MemberVO you = new MemberVO();
		if(room.getBuyerId() != null && room.getSellerId() != null) {			
			if(room.getBuyerId().equals(principalDetails.getUsername())) {
				youId = room.getSellerId();
				you = memberService.selectUser(youId);
			}
			else {
				youId = room.getBuyerId();
				you = memberService.selectUser(youId);
			}
		}
		System.out.println(you);
		
		model.addAttribute("you", you);
		model.addAttribute("member", principalDetails.getMemberVO());
		model.addAttribute("room", room);
		model.addAttribute("result", map);
		
		return "user/trade/tradeChat";
	}
	
	//중고거래 채팅저장
	@PostMapping("/member/insertTradeChat")
	@ResponseBody 
	public Map<String, Object> insertTradeChat(@RequestBody String str) {
		Map<String, Object> map = new HashMap<>();
		System.out.println("db저장값 : "+str);

		if(tradeChatService.insertChat(str) > 0) {
			map.put("result", "success");
		}
		else {
			map.put("result", "fail");
		}
		return map;
	}
	
	
	//나의 중고거래채팅리스트
	@GetMapping("/member/chatList")
	public String chatListPage(@AuthenticationPrincipal PrincipalDetails principalDetails,
							Model model) {
		Map<String, Object> map = tradeChatService.getMyChattings(principalDetails.getUsername());
		
		model.addAttribute("result", map);
		model.addAttribute("userId", principalDetails.getUsername());
		return "user/mypage/chatList";
	}
	
	//메시지 모두 읽음 표시
	@GetMapping("checkMessageAll")
	@ResponseBody
	public Map<String, Object> checkMessageAll(@AuthenticationPrincipal PrincipalDetails principalDetails,
											TradeChatVO vo) {
		System.out.println("컨트롤러");
		vo.setMemberId(principalDetails.getUsername());
		tradeChatService.updateAllChat(vo);
		Map<String, Object> map = new HashMap<>();
		map.put("result", "success");
		return map;
	}
	
	
	//실시간채팅 읽음 완료
	@GetMapping("checkMessageRead")
	@ResponseBody
	public String checkMessageRead(TradeChatVO vo) {
		if(tradeChatService.updateRead(vo) > 0 ) {
			return "success";
		}
		else return "fail";
		
	}
	
	//안읽은 메시지 있는지
	@GetMapping("BooleanMsgCk")
	@ResponseBody
	public Map<String, Object> BooleanMsgCk(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		String memberId = principalDetails.getUsername();
		Map<String, Object> map = tradeChatService.checkNew(memberId);
		return map;
	}
	
	//채팅방 나가기
	@GetMapping("/outChat")
	@ResponseBody
	public String outChat(@AuthenticationPrincipal PrincipalDetails principalDetails,
						String type, int roomId) {
		System.out.println(type);
		TradeChatVO vo = new TradeChatVO();
		vo.setMemberId(principalDetails.getUsername());
		vo.setType(type);
		vo.setTradeChatRoomId(roomId);
		System.out.println(vo);
		if(tradeChatService.outChatRoom(vo) > 0) {
			return "success";
		}
		else return "fail";
	}
	
	
}
