package com.team.prj.users.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team.prj.board.service.BoardVO;
import com.team.prj.cart.service.CartVO;
import com.team.prj.classes.service.ClassVO;
import com.team.prj.classreserve.service.ClassReserveVO;
import com.team.prj.comment.service.CommentVO;
import com.team.prj.hospital.service.HospitalVO;
import com.team.prj.like.service.LikesVO;
import com.team.prj.orders.service.OrderVO;
import com.team.prj.pet.service.PetVO;
import com.team.prj.photo.service.PhotoVO;
import com.team.prj.scrap.service.ScrapVO;
import com.team.prj.state.service.StateVO;
import com.team.prj.users.mapper.UsersMapper;

@Service
public class UsersServiceImpl implements UsersService {
	@Autowired
	private UsersMapper map;

	// 전체 유저 조회
	@Override
	public List<UsersVO> usersSelectList() {
		return map.usersSelectList();
	}

	// 유저 단건 조회
	@Override
	public UsersVO usersSelect(UsersVO vo) {
		return map.usersSelect(vo);
	}

	// 유저 등록
	@Override
	public int usersInsert(UsersVO vo) {
		return map.usersInsert(vo);
	}

	// 유저 수정
	@Override
	public int usersUpdate(UsersVO vo) {
		return map.usersUpdate(vo);
	}

	// 유저 삭제
	@Override
	public int usersDelete(UsersVO vo) {
		return map.usersDelete(vo);
	}

	// 주문 내역 조회
	@Override
	public List<OrderVO> orderList(OrderVO vo) {
		return map.orderList(vo);
	}

	// 사진 불러오기
	@Override
	public List<PhotoVO> photoList(PhotoVO vo) {
		return map.photoList(vo);
	}

	// 작성 게시글 조회
	@Override
	public List<BoardVO> boardList(BoardVO vo) {
		return map.boardList(vo);
	}

	// 작성 댓글 조회
	@Override
	public List<CommentVO> commentList(CommentVO vo) {
		return map.commentList(vo);
	}

	// 장바구니 조회
	@Override
	public List<CartVO> cartList(CartVO vo) {
		return map.cartList(vo);
	}

	// 수강 내역 조회
	@Override
	public List<ClassVO> classList(ClassReserveVO vo) {
		return map.classList(vo);
	}

	// 위시리스트 조회
	@Override
	public List<LikesVO> likeList(ScrapVO vo) {
		return map.likeList(vo);
	}

	// 위시리스트 삭제
	@Override
	public int likeDelete(ScrapVO vo) {
		return map.likeDelete(vo);
	}

	// 전체 스크랩 내역 조회
	@Override
	public List<ScrapVO> scrapList(ScrapVO vo) {
		return map.scrapList(vo);
	}

	// 병원 스크랩 조회
	@Override
	public List<ScrapVO> hospitalScrap(ScrapVO vo) {
		return map.hospitalScrap(vo);
	}

	// 장례 스크랩 조회
	@Override
	public List<ScrapVO> funeralScrap(ScrapVO vo) {
		return map.funeralScrap(vo);
	}

	// 숙박 스크랩 조회
	@Override
	public List<ScrapVO> accomoScrap(ScrapVO vo) {
		return map.accomoScrap(vo);
	}

	// 커뮤니티 스크랩 조회
	@Override
	public List<ScrapVO> communityScrap(ScrapVO vo) {
		return map.communityScrap(vo);
	}

	// 일반회원 검색 / 1005 지혜 추가
	@Override
	public List<UsersVO> userSearch(String key, String val) {
		return map.userSearch(key, val);
	}

	// 작성 글 삭제
	@Override
	public int usersBoardDelete(BoardVO vo) {
		return map.usersBoardDelete(vo);
	}

	// 작성 댓글 삭제
	@Override
	public int usersCommentDelete(CommentVO vo) {
		return map.usersCommentDelete(vo);
	}

	// 스크랩 삭제
	@Override
	public int usersScrapDelete(ScrapVO vo) {
		return map.usersScrapDelete(vo);
	}

	
}
