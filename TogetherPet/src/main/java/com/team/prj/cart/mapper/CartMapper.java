package com.team.prj.cart.mapper;

import java.util.List;
import java.util.Map;

import com.team.prj.cart.service.CartVO;

public interface CartMapper {
	List<Map<String, Object>> cartList(CartVO vo);
	
	int insertCart(CartVO vo);
	int updateCart(CartVO vo);
	int deleteCart(CartVO vo);	
}