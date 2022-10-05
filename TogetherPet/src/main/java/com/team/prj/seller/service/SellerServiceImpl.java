package com.team.prj.seller.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team.prj.admin.service.ProfitVO;
import com.team.prj.goods.service.GoodsVO;
import com.team.prj.seller.mapper.SellerMapper;

@Service
public class SellerServiceImpl implements SellerService {
	@Autowired
	private SellerMapper map;
	
	// 판매자 전체 조회
	@Override
	public List<SellerVO> sellerSelectList() {
		return map.sellerSelectList();
	}

	// 판매자 단건 조회
	@Override
	public SellerVO sellerMyPage(SellerVO vo) {
		return map.sellerMyPage(vo);
	}

	// 판매자 등록
	@Override
	public int sellerInsert(SellerVO vo) {
		return map.sellerInsert(vo);
	}

	// 판매자 수정
	@Override
	public int sellerUpdate(SellerVO vo) {
		return map.sellerUpdate(vo);
	}

	// 판매자 삭제
	@Override
	public int sellerDelete(SellerVO vo) {
		return map.sellerDelete(vo);
	}

	// 판매자 상품 조회
	@Override
	public List<GoodsVO> goodsList(GoodsVO vo) {
		return map.goodsList(vo);
	}

	// 판매자 상품 등록
	@Override
	public int goodsInsert(GoodsVO vo) {
		return map.goodsInsert(vo);
	}

	@Override
	public List<ProfitVO> sellerProfitList(SellerVO vo) {
		return map.sellerProfitList(vo);
	}

	

}
