package com.holoyolo.app.holopayHistory.service.Impl;

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
		
//		LocalTime hour = loca
		
		
		// Request Header 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("ApiNm","DrawingTransfer");
		headers.set("Tsymd","");
		headers.set("Trtm","DrawingTransfer");
		headers.set("Iscd",IsTuno);
		headers.set("FintechApsno","001");
		headers.set("ApiSvcCd","DrawingTransferA");
		headers.set("IsTuno","DrawingTransfer");
		
//		{
//			  "Header": {
//			    "ApiNm": "DrawingTransfer",
//			    "Tsymd": "오늘날짜를입력하세요",
//			    "Trtm": "112428",
//			    "Iscd": "기관코드를입력하세요",
//			    "FintechApsno": "001",
//			    "ApiSvcCd": "DrawingTransferA",
//			    "IsTuno": "0000",
//			    "AccessToken": "인증키를입력하세요"
//			  },
//			  "FinAcno": "핀어카운트",
//			  "Tram": "거래금액",
//			  "DractOtlt": "출금계좌인자내용"
//			}
		// Request Body 설정
		JSONObject requestBody = new JSONObject();
		requestBody.put("key", "value");

		// Request Entity 생성
		HttpEntity<String> entity = new HttpEntity<String>(requestBody.toString(), headers);
	}

}
