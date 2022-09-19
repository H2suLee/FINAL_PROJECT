package com.team.prj.cart.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.team.prj.cart.mapper.CartMapper;

public class CartServiceImpl implements CartService{
	@Autowired
	private CartMapper map;
	@Override
	public List<Map<String, Object>> cartList(CartVO vo) {
		// TODO Auto-generated method stub
		return map.cartList(vo);
	}

	@Override
	public int insertCart(CartVO vo) {
		// TODO Auto-generated method stub
		return map.insertCart(vo);
	}

	@Override
	public int updateCart(CartVO vo) {
		// TODO Auto-generated method stub
		return map.updateCart(vo);
	}

	@Override
	public int deleteCart(CartVO vo) {
		// TODO Auto-generated method stub
		return map.deleteCart(vo);
	}
	
}