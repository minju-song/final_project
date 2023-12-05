package com.holoyolo.app.sms.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;
import com.holoyolo.app.member.service.impl.MemberServiceImpl;
import com.holoyolo.app.sms.service.SmsService;

import lombok.extern.log4j.Log4j2;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Log4j2
@Controller
public class SmsController {
	
	@Autowired
	SmsService smsService;
	@Autowired
	MemberService memberService;
	
	final DefaultMessageService messageService;
	
	@Value("${coolsms.api.num}")
	private String phoneNum;

    public SmsController(@Value("${coolsms.api.key}") String apiKey,
    					 @Value("${coolsms.api.secret}") String apiSecret) {
    	
        // 반드시 계정 내 등록된 유효한 API 키, API Secret Key를 입력해주셔야 합니다!
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }
    
    @GetMapping("/sendSms")
    @ResponseBody
    public String sendJoinSms(HttpServletRequest request, String reception) {
        Message message = new Message();
        
        String result = "Fail";
        String numStr = smsService.getRandomNum(6); // 6자리 랜덤 번호 생성
    	log.warn("발송될 인증번호 ::: " + numStr);
        
        message.setFrom(phoneNum);		// 발신번호 입력(보내는 번호) - Sent
        message.setTo(reception);		// 수신번호 입력(받는 번호) - reception
        message.setText("[HoloYolo] 인증번호 : " + numStr + " 타인 유출로 인한 피해 주의");		// 내용

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);
        if(response.getStatusCode().equals("2000")) {
        	result = "Success";
        	// 세션에 인증번호 저장하기..
        	HttpSession session = request.getSession();
        	session.setAttribute("authNum", numStr);
        	return result;
        }
        return result;
    }
    
    @GetMapping("/checkAuthNum")
    @ResponseBody
    public String checkAuthNum(HttpServletRequest request, String authNum) {
    	HttpSession session = request.getSession();
    	String result = "Fail";
    	String num = (String) session.getAttribute("authNum");
    	log.warn("세션에 저장된 인증번호 ::: " + num);
    	log.warn("고객이 입력한 인증번호 ::: " + authNum);
    	
    	if(authNum.equals(num)) {
    		result = "Success";
    		session.invalidate();
    		return result;
    	}
    	
    	return result;
    }
    
    /*
     * 회원 휴대폰 변경
     */
    @GetMapping("/checkAuthNum/phoneUpdate")
    @ResponseBody
    public String checkAuthNumPhoneUpdate(@AuthenticationPrincipal PrincipalDetails principalDetails,
    									  HttpServletRequest request, MemberVO memberVO) {
    	memberVO.setMemberId(principalDetails.getUsername());
    	HttpSession session = request.getSession();
    	String result = "Fail";
    	
    	String num = (String) session.getAttribute("authNum");
    	log.warn("세션에 저장된 인증번호 ::: " + num);
    	log.warn("고객이 입력한 인증번호 ::: " + memberVO.getAuthNum());
    	log.warn("요청 아이디 ::: " + memberVO.getMemberId());
    	log.warn("요청 휴대폰 ::: " + memberVO.getPhone());
    	
    	if(memberVO.getAuthNum().equals(num)) {
    		result = "Success";
    		session.removeAttribute("authNum");
    		memberService.updateMemberInfo(memberVO);
    		return result;
    	}
    	
    	return result;
    }
    
}
