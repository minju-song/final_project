package com.holoyolo.app.holopayHistory.service.Impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.holoyolo.app.holopayHistory.mapper.HoloPayHistoryMapper;
import com.holoyolo.app.holopayHistory.service.HoloPayHistoryService;
import com.holoyolo.app.holopayHistory.service.HoloPayHistoryVO;

@Service
public class HoloPayHistoryServiceImpl implements HoloPayHistoryService {

	@Autowired
	HoloPayHistoryMapper holopayHistoryMapper;

	@Override
	public List<HoloPayHistoryVO> holopayHistoryList() {

		return holopayHistoryMapper.holopayHistoryList();
	}

	@Override
	public HoloPayHistoryVO holopayInfo() {

		return holopayHistoryMapper.holopayInfo();
	}

	@Override
	public int insertHolopayHistory() {

		return holopayHistoryMapper.insertHolopayHistory();
	}

	@Autowired
	RestTemplate restTemplate;

	public void rechargePay() {
		String url = "https://developers.nonghyup.com/DrawingTransfer.nh";
		ResponseEntity response = restTemplate.getForEntity(url, String.class);
		System.out.println(response.getBody());
		
		String IsTuno = String.valueOf(System.currentTimeMillis()/1000+((int) Math.random()*9+1)) ;
		
		LocalTime now = LocalTime.now();
		LocalDate date = LocalDate.now();
		
		String year = String.valueOf(date.getYear());
		String mon = String.valueOf(now.getMinute());
		String day = String.valueOf(now.getSecond());
		String hour = String.valueOf(now.getHour());
		String min = String.valueOf(now.getMinute());
		String sec = String.valueOf(now.getSecond());
		
		// Request Header 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		JSONObject requestBody = new JSONObject();
		requestBody.put("key", "value");
		requestBody.put("ApiNm","DrawingTransfer");
		headers.set("Tsymd",year+mon+day);
		headers.set("Trtm",hour+min+sec);
		headers.set("Iscd",IsTuno);
		headers.set("FintechApsno","001");
		headers.set("ApiSvcCd","DrawingTransferA");
		headers.set("IsTuno","DrawingTransfer");

//		{	
//		    "Header":{
//		        "ApiNm":"DrawingTransfer",
//		        "Tsymd":"20191129",
//		        "Trtm":"125237",
//		        "Iscd":"900001",
//		        "FintechApsno":"001",
//		        "ApiSvcCd":"DrawingTransferA",
//		        "IsTuno":"201911290000000001", 
//		        "AccessToken": "6500e3a81fe2deafd996ea437b6a4b7cfbd04a3ab7e26480b431fdf3ccf3b39b"
//		    },
//		"FinAcno":"00820109000010001413",
//		"Tram":"1000000",
//		"DractOtlt":"테스트",
//		"MractOtlt":"테스트"
//		}
//		Response example
		// Request Entity 생성
		HttpEntity<String> entity = new HttpEntity<String>(requestBody.toString(), headers);
	}

}
