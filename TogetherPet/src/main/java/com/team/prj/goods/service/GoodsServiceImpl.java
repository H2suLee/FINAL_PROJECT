package com.team.prj.goods.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team.prj.goods.mapper.GoodsMapper;
import com.team.prj.orders.service.OrderVO;
import com.team.prj.photo.service.PhotoVO;
import com.team.prj.review.service.ReviewVO;
import com.team.prj.state.service.StateVO;

@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private GoodsMapper map;

	@Override
	public List<GoodsVO> goodsSelectAll(String key) {
		return map.goodsSelectAll(key);
	}

	@Override
	public GoodsVO goodsSelectOne(GoodsVO vo) {
		return map.goodsSelectOne(vo);
	}

	@Override
	public int insertGoods(GoodsVO vo) {
		return map.insertGoods(vo);
	}

	@Override
	public int updateGoods(GoodsVO vo) {
		return map.updateGoods(vo);
	}

	@Override
	public int deleteGoods(GoodsVO vo) {
		return map.deleteGoods(vo);
	}

	// 검색 기능(1004 선희 수정 => String value -> String val로 변경)
	@Override
	public List<GoodsVO> goodsSearch(String key, String val, String start, String end) {
		return map.goodsSearch(key, val, start, end);
	}

	// 오더 검색 기능(1004 선희 추가)
	@Override
	public List<OrderVO> orderSearch(String key, String val, String start, String end) {
		return map.orderSearch(key, val, start, end);
	}

	// 반품교환 검색 기능(1005 선희 추가)
	@Override
	public List<StateVO> stateSearch(String key, String val, String start, String end) {
		return map.stateSearch(key, val, start, end);
	}

	// 판매완료 상품 검색 기능(1005 선희 추가)
	@Override
	public List<OrderVO> goodsSellSearch(String key, String val, String start, String end) {
		return map.goodsSellSearch(key, val, start, end);
	}

	@Override
	public int goodsHitUpdate(GoodsVO vo) {
		return map.goodsHitUpdate(vo);
	}

	@Override
	public List<PhotoVO> goodsPhotoList(GoodsVO vo) {
		return map.goodsPhotoList(vo);
	}

	@Override
	public List<Map<Integer, Integer>> reviewCount() {
		return map.reviewCount();
	}

	@Override
	public List<ReviewVO> reviewList(GoodsVO vo) {
		return map.reviewList(vo);
	}

	// 배송 조회(1003 선희 추가)
	@Override
	public List<OrderVO> deliveryList(OrderVO vo) {
		return map.deliveryList(vo);
	}

	// 배송 상태 업데이트 => 상품준비중(1003 선희 추가)
	@Override
	public int deliveryReadyUpdate(OrderVO vo) {
		return map.deliveryReadyUpdate(vo);
	}

	// 배송 상태 업데이트 => 배송지시(1004 선희 추가)
	@Override
	public int deliveryUpdate(OrderVO vo) {
		return map.deliveryUpdate(vo);
	}

	// 배송 상태 업데이트 => 배송완료(1005 선희 추가)
	@Override
	public int deliveryDone(OrderVO vo) {
		return map.deliveryDone(vo);
	}

	// 반품/교환 상품 조회(1004 선희 추가)
	@Override
	public List<OrderVO> sellerCancelList(OrderVO vo) {
		return map.sellerCancelList(vo);
	}

	// 판매완료 상품 조회(1004 선희 추가)
	@Override
	public List<OrderVO> sellerDoneList(OrderVO vo) {
		return map.sellerDoneList(vo);
	}

	// 상품 조회(state=0 (미 승인) 1004 추가 / 지혜)
	@Override
	public List<GoodsVO> goodsList(String key) {
		// TODO Auto-generated method stub
		return map.goodsList(key);
	}

	// 반품요청 상태 업데이트(1004 선희 추가)
	@Override
	public int cancelUpdate(StateVO vo) {
		return map.cancelUpdate(vo);
	}

	// 교환요청 상태 업데이트(1004 선희 추가)
	@Override
	public int changeUpdate(StateVO vo) {
		return map.changeUpdate(vo);
	}

	// (관리자 페이지) 상품 검색 (1017 지혜 추가)
	@Override
	public List<GoodsVO> goodsSearchAd(String key, String val) {
		return map.goodsSearchAd(key, val);
	}




}
