package com.team.prj.users.mapper;

import java.util.List;

import com.team.prj.board.service.BoardVO;
import com.team.prj.cart.service.CartVO;
import com.team.prj.classes.service.ClassVO;
import com.team.prj.classreserve.service.ClassReserveVO;
import com.team.prj.comment.service.CommentVO;
import com.team.prj.like.service.LikesVO;
import com.team.prj.orders.service.OrderVO;
import com.team.prj.photo.service.PhotoVO;
import com.team.prj.scrap.service.ScrapVO;
import com.team.prj.state.service.StateVO;
import com.team.prj.users.service.UsersVO;

public interface UsersMapper {
	// 전체 리스트
	List<UsersVO> usersSelectList();

	// 단건 조회
	UsersVO usersSelect(UsersVO vo);

	// 등록
	int usersInsert(UsersVO vo);

	// 수정
	int usersUpdate(UsersVO vo);

	// 삭제
	int usersDelete(UsersVO vo);

	// 주문 내역 조회
	List<OrderVO> orderList(OrderVO vo);

	// 반품 신청 폼에 주문내역 불러오기
	List<OrderVO> orderCanList(OrderVO vo);

	// 사진 불러오기
	List<PhotoVO> photoList(PhotoVO vo);

	// 장바구니 조회
	List<CartVO> cartList(CartVO vo);

	// 수강내역 조회
	List<ClassVO> classList(ClassReserveVO vo);

	// 위시리스트 조회
	List<LikesVO> likeList(LikesVO vo);

	// 내 스크랩조회
	List<ScrapVO> scrapList(ScrapVO vo);

	// 작성 글조회
	List<BoardVO> boardList(BoardVO vo);

	// 작성 댓글조회
	List<CommentVO> commentList(CommentVO vo);

	// 반품 신청 등록
	int cancelInsert(StateVO vo);
}
