package com.holoyolo.app.mail.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.club.service.ClubVO;
import com.holoyolo.app.clubMember.service.ClubMemberService;
import com.holoyolo.app.clubMember.service.ClubMemberVO;
import com.holoyolo.app.mail.service.EmailService;
import com.holoyolo.app.mail.service.EmailVO;
import com.holoyolo.app.member.service.MemberVO;
import com.holoyolo.app.trade.service.TradeVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EmailController {

	@Autowired
	private final EmailService emailService;
	
	@Autowired
	private final ClubMemberService clubMemberService;
	
	
	/**
	 * 임시 비밀번호 발급
	 * @param email
	 * @return
	 */
	@PostMapping("/sendmail/password")
	public void sendPasswordMail(MemberVO memberVO) {
		EmailVO emailVO = EmailVO.builder()
				.to(memberVO.getMemberId())
				.subject("[holoyolo] 임시 비밀번호 발급 메일입니다.")
				.build();
		
		emailService.sendMail(emailVO, null, "password");
	}
	
	/**
	 * 회원가입 축하메일
	 * @param memberVO
	 * @return
	 */
	@PostMapping("/sendmail/joinmail")
	public void sendJoinMail(MemberVO memberVO) {
		EmailVO emailVO = EmailVO.builder()
				.to(memberVO.getMemberId())
				.subject("[holoyolo]" + memberVO.getMemberName() + "님, 회원가입을 축하드립니다")
				.build();
		
		emailService.sendMail(emailVO, null, "join");
	}
	
	//클럽가입요청
	//@RequestMapping(value = "/sendmail/requestclub", method = RequestMethod.POST)
	@PostMapping("/sendmail/requestclub")
	public void sendRequestMail(@AuthenticationPrincipal PrincipalDetails principalDetails,@RequestBody ClubVO vo) {
		EmailVO emailVO = EmailVO.builder()
				.to(vo.getClubLeader())
				.subject(vo.getClubName()+"모임에 대한 가입요청 메일입니다.")
				.clubId(vo.getClubId())
				.clubName(vo.getClubName())
				.reqId(principalDetails.getUsername())
				.text(vo.getText())
				.type(vo.getType())
				.build();
		
		System.out.println("이메일객체"+vo);
		
		ClubMemberVO cmvo = new ClubMemberVO();
		cmvo.setClubId(vo.getClubId());
		cmvo.setMemberId(principalDetails.getUsername());
		
		if(vo.getType().equals("join")) {			
			if(clubMemberService.reqClub(cmvo) > 0) {
				System.out.println("성공");
			}
			else {
				System.out.println("실패");
			}
		}
		else {
			if(clubMemberService.reqRejoin(cmvo) > 0) {
				System.out.println("성공");
			}
			else {
				System.out.println("실패");
			}
		}
		emailService.sendRequest(emailVO);
	}
	
	@PostMapping("/sendmail/mandate")
	@ResponseBody
	public String mandate(@AuthenticationPrincipal PrincipalDetails principalDetails,@RequestBody ClubVO vo) {
		System.out.println("들어온 값 : "+vo);
		EmailVO emailVO = EmailVO.builder()
				.to(vo.getClubLeader())
				.subject(vo.getClubName()+"모임에 대한 모임장 위임요청 메일입니다.")
				.clubId(vo.getClubId())
				.clubName(vo.getClubName())
				.reqId(principalDetails.getUsername())
				.text(vo.getText())
				.type(vo.getType())
				.leader(vo.getClubLeader())
				.build();
		
		if(emailService.mandate(emailVO).equals("success")) {
			return "success";
		}
		else return "fail";
	}
	
	@PostMapping("/sendMail/deleteReason")
	@ResponseBody
	public String deleteReason(@RequestBody ClubVO vo) {
		System.out.println("들어온 값 : "+vo);
		EmailVO emailVO = EmailVO.builder()
				.to("kongom2@naver.com")
				.subject(vo.getClubName() + "모임은 삭제되었습니다.")
				.clubId(vo.getClubId())
				.clubName(vo.getClubName())
				.text(vo.getText())
				.build();
		
		if(emailService.deleteReason(emailVO).equals("success")) {
			return "success";
		}
		else return "fail";
	}
	
	/**
	 * 중고거래 약속잡기 메일
	 * @param tradeVO
	 * @return
	 */
	@PostMapping("/sendmail/trade/promise")
	public void sendPromise(@AuthenticationPrincipal PrincipalDetails principalDetails
						    , TradeVO tradeVO) {
		EmailVO emailVO = EmailVO.builder()
				.to(tradeVO.getSellerId())
				.subject("[holoyolo] 약속신청이 도착했어요!")
				.build();
		
		tradeVO.setNickname(principalDetails.getName()); 	//구매자 닉네임
		tradeVO.setBuyerId(principalDetails.getUsername());	//구매자 아이디
		emailService.sendMail(emailVO, tradeVO, "promise");
	}
	
	/**
	 * 중고거래 약속수락 메일
	 * @param tradeVO
	 * @return
	 */
	@PostMapping("/sendmail/trade/promiseOk")
	public void sendPromiseOk(@AuthenticationPrincipal PrincipalDetails principalDetails
						    , TradeVO tradeVO) {
		EmailVO emailVO = EmailVO.builder()
				.to(tradeVO.getBuyerId())
				.subject("[holoyolo] 약속신청이 수락되었어요!")
				.build();
		
		tradeVO.setSellerId(principalDetails.getUsername());	//판매자 아이디
		emailService.sendMail(emailVO, tradeVO, "promiseOk");
	}
}
