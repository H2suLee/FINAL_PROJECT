package com.team.prj.admin.service;

import java.util.List;

import com.team.prj.classes.service.ClassVO;
import com.team.prj.goods.service.GoodsVO;
import com.team.prj.users.service.UsersVO;

public interface adminService {
	List<adminVO> adminSelectList(); // 관리자 목록
	adminVO adminSelect(adminVO vo); // 관리자 단건조회
	int adminUpdate(adminVO vo); // 관리자 수정
	
	int postUpdate(GoodsVO vo); // 상품 상태 업데이트
	int cpostUpdate(ClassVO vo); // 클래스 상태 업데이트
	
	int goodsRefuse(GoodsVO vo); // 상품 상태 업데이트 - 반려
	int classRefuse(ClassVO vo); // 클래스 상태 업데이트 - 반려
	
	void uStateUpdate(UsersVO vo); // 일반회원 상태 업데이트
	
	
}
