package com.team.prj.classreserve.service;

import java.util.Date;

import lombok.Data;

@Data
public class ClassReserveVO {
	int reserveNo;
	String dt;
	Date orderDate;
	String pay;
	String payYn;
	int userNo;
	int classNo;
	int minusPrice;
	int totalPrice;
	
	Date minusDate;
	int classOptionNo;
	String call; //유저전화번호
	String name; //유저이름
	int money; //적립금
	String className; //클래스 이름
	
	// 0927 선희 추가
	private String address;
	private String tName;
	
	// 0928 희수 minusYn 타입 수정 (int > String)
	String minusYn;
	
}
