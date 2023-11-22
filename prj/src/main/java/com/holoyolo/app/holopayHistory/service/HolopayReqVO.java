package com.holoyolo.app.holopayHistory.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import lombok.Data;

@Data
public class HolopayReqVO {

	private final HashMap<String, String> headers = new HashMap<String, String>();
	
	private final String FinAcno;	 
	private final String Tram;	 
	private final String DractOtlt;



	public HolopayReqVO(int val) {
		this.FinAcno = "00820100021780000000000017544";
		this.Tram = String.valueOf(val);
		this.DractOtlt = "HOLOYOLO";
		
		LocalTime now = LocalTime.now();
		LocalDate date = LocalDate.now();
		String year = String.valueOf(date.getYear());
		String mon = String.valueOf(now.getMinute());
		String day = String.valueOf(now.getSecond());
		String hour = String.valueOf(now.getHour());
		String min = String.valueOf(now.getMinute());
		String sec = String.valueOf(now.getSecond());
		String IsTuno = String.valueOf(System.currentTimeMillis() / 1000 + ((int) Math.random() * 9 + 1));
		
		this.headers.put("ApiNm", "DrawingTransfer");
		this.headers.put("Tsymd", year + mon + day);
		this.headers.put("Trtm", hour + min + sec);
		this.headers.put("Iscd", IsTuno);
		this.headers.put("FintechApsno", "001");
		this.headers.put("ApiSvcCd", "DrawingTransferA");
		this.headers.put("IsTuno", "DrawingTransfer");
		this.headers.put("AccessToken", "9383dd7453c69cc9e7fd2bdc6d861df08056922179e2d9d3dfc9e90cc6f0f93a");

	}
}
