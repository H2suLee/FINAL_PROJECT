package com.team.prj.admin.service;

import java.util.Date;

import lombok.Data;

@Data
public class ProfitVO {

	private String minusDt;
	private String orderDt;
	private int no, userNo;
	private String userName;
	private int sellerNo;
	private String sellerName;
	private String categoryId;
	private String name; // 거래품명
	private int totalPrice;
	private int minusPrice;
	private int profit;
	private String minusYn;
}
