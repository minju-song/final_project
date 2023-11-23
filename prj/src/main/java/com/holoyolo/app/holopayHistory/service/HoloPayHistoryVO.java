package com.holoyolo.app.holopayHistory.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class HoloPayHistoryVO {
	//holopay history id
 private int hpHistoryId;
 //member id = membervo
 private String memberId;
 //holopay date 거래일자
 @DateTimeFormat(pattern = "yyMMdd")
 private Date hpDate;
 //holopay type 거래유형(충전/반환)
 private String hpType;
  //금액 
 private int price;
 //info
 private String holopayComment;
 //거래 아이디
 private int tradeId;
//회원 결재정보
 private int memberFinanceId;
}
