package com.holoyolo.app.sms.service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class SmsService {
	
	public String getRandomNum(int size) {
		Random rand  = new Random();
		String numStr = "";
		for(int i=0; i<size; i++) {
			String ran = Integer.toString(rand.nextInt(10));
			numStr+=ran;
		}
		return numStr;
	}

}
