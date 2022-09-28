package com.team.prj.seller.service;

import java.util.List;

import com.team.prj.goods.service.GoodsVO;

public interface SellerService {
	// 판매자 전체 조회
	List<SellerVO> sellerSelectList();

	// 판매자 단건 조회
	SellerVO sellerMyPage(SellerVO vo);

	// 판매자 등록
	int sellerInsert(SellerVO vo);

	// 판매자 정보 수정
	int sellerUpdate(SellerVO vo);

	// 판매자 삭제
	int sellerDelete(SellerVO vo);

	// 판매자 상품 조회
	List<GoodsVO> goodsList(GoodsVO vo);
	
	// 판매자 상품 등록
	int goodsInsert(GoodsVO vo);
}
