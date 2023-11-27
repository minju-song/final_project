package com.holoyolo.app.accBookHistory.service.apiVO;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

import lombok.Data;

@Data
public class CardRequestBodyVO {
	private final HashMap<String, String> Header = new HashMap<String, String>();
    private final String FinCard;
    private final String IousDsnc;
    private final String Insymd;
    private final String Ineymd;
    private final String PageNo;
    private final String Dmcnt;
    
    public CardRequestBodyVO() {
    	this.FinCard = "00829101234560000112345678919";
    	this.IousDsnc = "1";
    	this.PageNo = "1";
    	this.Dmcnt = "15";
    	this.Insymd = "20191105";
    	this.Ineymd = "20191109";
    	
    	this.Header.put("ApiNm", "InquireCreditCardAuthorizationHistory");
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat timeformat = new SimpleDateFormat("HHmmss");
    	Date now = new Date();
    	String now_dt = format.format(now);
    	String now_time = timeformat.format(now);
    	this.Header.put("Tsymd", now_dt);
    	this.Header.put("Trtm", now_time);
    	this.Header.put("Iscd", "002177");
    	this.Header.put("FintechApsno", "001");
    	this.Header.put("ApiSvcCd", "CardInfo");
    	String rand = Integer.toString((int) (Math.random() * 10000000));
    	
    	this.Header.put("IsTuno", rand);
    	this.Header.put("AccessToken", "da36d221e7e64a34021c1e2f8b276de908de14bdbdba3db6c02a7ae1b6620ef2");
    }
    
   
}
