package com.holoyolo.app.holopayHistory.service.api;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import lombok.Data;

@Data
public class HolopayWithdrawalApiVO {

	private final HashMap<String, String> Header = new HashMap<String, String>();

	private String Bncd;
	private String Acno;
	private String Tram;
	private String DractOtlt;
	private String MractOtlt;
	
	
	public HolopayWithdrawalApiVO(String tram, String Acno){
		
		
		this.Bncd = "011";
		this.Acno = Acno;
		this.Tram = tram;
		this.DractOtlt ="HP2";
		this.MractOtlt="HOLOYOLO";	
		
		LocalTime now = LocalTime.now();
		LocalDate date = LocalDate.now();
		String year = String.valueOf(date.getYear());
        String mon = String.valueOf(date.getMonthValue());
        String day = String.valueOf(date.getDayOfMonth());
        String hour = String.format("%02d", now.getHour());
        String min = String.format("%02d", now.getMinute());
        String sec = String.format("%02d", now.getSecond());
		String IsTuno = String.valueOf((int) Math.floor(Math.random() * 9 + 1) +hour + min + sec) ;
		String Tsymd = year + mon + day;
		String Trtm = hour + min + sec;
				
		this.Header.put("ApiNm", "ReceivedTransferAccountNumber");
		this.Header.put("Tsymd", Tsymd);
		this.Header.put("Trtm", Trtm); 
		this.Header.put("Iscd", "002178");
		this.Header.put("FintechApsno", "001");
		this.Header.put("ApiSvcCd", "DrawingTransferA");
		this.Header.put("IsTuno", IsTuno);
		this.Header.put("AccessToken", "9383dd7453c69cc9e7fd2bdc6d861df08056922179e2d9d3dfc9e90cc6f0f93a");	

		}
}
