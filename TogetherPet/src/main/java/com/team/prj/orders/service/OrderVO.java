package com.team.prj.orders.service;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderVO {
	private int orderNo, amount, totalPrice, minusPrice, userNo, goodsNo, deliveryNo;
	private String address, message, call, name, pay, payYn, deliveryState, minusYn;
	private Date dt;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+9")
	private Date minusDate;
	
	// 0923 희수 추가
	private int cartNo, money; 
	
	//0925 희수 추가
	private int sellerNo;
	private String goodsName;

	//0927 희수가 추가
	private String postcode; // 우편번호
	private String da; // 상세주소

	// 0927 선희 추가
	private String thumb; // 썸네일
	
	// 1003 선희 추가
	private Date cancelDt; // 반품신청일자
	private Date changeDt; // 교환신청일자
	
	// 1004 선희 추가
	private String state; // 반품,교환상태
	
	// 1010 희수 추가
	private String nickname;
	private int price;
}

