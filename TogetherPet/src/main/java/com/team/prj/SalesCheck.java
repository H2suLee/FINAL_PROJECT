package com.team.prj;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.team.prj.classreserve.service.ClassReserveService;
import com.team.prj.orders.service.OrderService;

@Component
public class SalesCheck {
	@Autowired
	OrderService order;
	@Autowired
	ClassReserveService cr;
	
//	@Scheduled(cron="0 0 0 * * * ") // 매일 자정
	@Scheduled(cron="0 0 11 * * ?") // 매일 오전 11시
	public void salesCheck() {
		order.updateMinusYN();
		System.out.println("################################# 상품 정산완료 #################################");
		cr.updateMinusYN();
		System.out.println("################################# 클래스 정산완료 #################################");
	}
}
