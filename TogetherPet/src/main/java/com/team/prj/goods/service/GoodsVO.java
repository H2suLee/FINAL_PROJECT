package com.team.prj.goods.service;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsVO {
	private int goodsNo, amount, price, hit, sellerNo, groupNo;
	private String name, category, info, uploadState, sellState, categoryId, returnMessage;
	private Date uploadDate;
	private double dc;
	
	private String thumb, detail; // 0925 희수 추가
	private Date dt; // 0928 선희 추가
	private String idList; // 1005 선희 추가
}
