package com.holoyolo.app.holopayHistory.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import lombok.Data;

@Data
public class HolopayReqVO {

	private final HashMap<String, String> Header = new HashMap<String, String>();
	private final String FinAcno;
	private final String Tram;
	private final String DractOtlt;

	public HolopayReqVO(String val) {
		this.FinAcno ="00820100021780000000000017544";
		this.Tram =val;
		this.DractOtlt ="홀로페이 충전";
		LocalTime now = LocalTime.now();
		LocalDate date = LocalDate.now();
		String year = String.valueOf(date.getYear());
		String mon = String.valueOf(now.getMinute());
		String day = String.valueOf(now.getSecond());
		String hour = String.valueOf(now.getHour());
		String min = String.valueOf(now.getMinute());
		String sec = String.valueOf(now.getSecond());
		String IsTuno = String.valueOf((Math.random() * 9 + 1))+hour + min + sec ;

		this.Header.put("ApiNm", "DrawingTransfer");
		this.Header.put("Tsymd", year + mon + day);
		this.Header.put("Trtm", hour + min + sec);
		this.Header.put("Iscd", "002178");
		this.Header.put("FintechApsno", "001");
		this.Header.put("ApiSvcCd", "DrawingTransferA");
		this.Header.put("IsTuno", IsTuno);
		this.Header.put("AccessToken", "9383dd7453c69cc9e7fd2bdc6d861df08056922179e2d9d3dfc9e90cc6f0f93a");

	}

}
