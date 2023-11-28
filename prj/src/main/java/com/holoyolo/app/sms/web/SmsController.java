package com.holoyolo.app.sms.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.sms.service.SmsService;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Controller
public class SmsController {
	
	@Autowired
	SmsService smsService;
	
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
    	System.out.println(numStr);
        
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
    	System.out.println("num :: " + num);
    	System.out.println("authNum :: " + authNum);
    	
    	if(authNum.equals(num)) {
    		result = "Success";
    		session.invalidate();
    		return result;
    	}
    	
    	return result;
    }
    
	/*
	 * @GetMapping("/sendPwd") public SingleMessageSentResponse sendPwdSms(String
	 * reception) { Message message = new Message();
	 * 
	 * String numStr = smsService.getRamdomPassword(10); System.out.println(numStr);
	 * 
	 * message.setFrom(phoneNum); // 발신번호 입력(보내는 번호) - Sent
	 * message.setTo(reception); // 수신번호 입력(받는 번호) - reception
	 * message.setText("[HoloYolo] 임시비밀번호 : " + numStr + " 타인 유출로 인한 피해 주의"); // 내용
	 * 
	 * SingleMessageSentResponse response = this.messageService.sendOne(new
	 * SingleMessageSendingRequest(message)); System.out.println(response);
	 * 
	 * return response; }
	 */

}
