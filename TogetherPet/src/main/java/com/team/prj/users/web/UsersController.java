package com.team.prj.users.web;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.team.prj.board.service.BoardService;
import com.team.prj.board.service.BoardVO;
import com.team.prj.calendar.service.CalendarService;
import com.team.prj.calendar.service.CalendarVO;
import com.team.prj.cart.service.CartService;
import com.team.prj.cart.service.CartVO;
import com.team.prj.classes.service.ClassVO;
import com.team.prj.classreserve.service.ClassReserveService;
import com.team.prj.classreserve.service.ClassReserveVO;
import com.team.prj.comment.service.CommentService;
import com.team.prj.comment.service.CommentVO;
import com.team.prj.like.service.LikesService;
import com.team.prj.like.service.LikesVO;
import com.team.prj.orders.service.OrderService;
import com.team.prj.orders.service.OrderVO;
import com.team.prj.pet.service.PetService;
import com.team.prj.pet.service.PetVO;
import com.team.prj.scrap.service.ScrapService;
import com.team.prj.scrap.service.ScrapVO;
import com.team.prj.state.service.StateService;
import com.team.prj.state.service.StateVO;
import com.team.prj.tutor.service.TutorService;
import com.team.prj.users.service.UsersService;
import com.team.prj.users.service.UsersVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // 생성자 주입
@Controller
public class UsersController {
	@Autowired
	private ServletContext servletContext;
	@Autowired
	private UsersService user; // 개인회원
	@Autowired
	private CartService cart; // 장바구니
	@Autowired
	private OrderService order; // 주문내역
	@Autowired
	private StateService state; // 반품교환상태
	@Autowired
	private CommentService comment; // 댓글조회
	@Autowired
	private BoardService board; // 작성글조회
	@Autowired
	private ScrapService scrap; // 스크랩 내역
	@Autowired
	private TutorService tutor; // 클래스 튜터
	@Autowired
	private LikesService like; // 위시리스트
	@Autowired
	private CalendarService cal; // 캘린더
	@Autowired
	private PetService pet; // 반려동물 정보
	@Autowired
	private ClassReserveService cr; // 수강내역

	@Value("${file.dir}")
	private String fileDir;

	// 개인 회원 리스트
	@RequestMapping("/usersSelect")
	public String usersSelect(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		UsersVO vo = (UsersVO) session.getAttribute("user");
		user.usersSelect(vo);
		return "users/usersSelect";
	}

	// 회원 정보 수정 폼 호출
	@RequestMapping("/usersUpdateForm")
	public String usersUpdateForm(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		UsersVO vo = new UsersVO();
		String id = (String) session.getAttribute("id");
		vo.setId(id);
		vo = user.usersSelect(vo);
		request.setAttribute("userList", vo);
		return "users/usersUpdateForm";
	}

	// 회원 정보 수정 처리
	@PostMapping("/usersUpdate")
	public String usersUpdate(UsersVO vo, HttpServletRequest request) {
		HttpSession session = request.getSession();
		user.usersUpdate(vo);
		vo = user.usersSelect(vo);
		session.setAttribute("user", vo);
		return "redirect:/usersSelect";
	}

	// 회원 탈퇴
	@RequestMapping("/usersDelete")
	public String usersDelete(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UsersVO vo = (UsersVO) session.getAttribute("user");
		user.usersDelete(vo);
		return "index";
	}

	// 마이페이지 장바구니
	@RequestMapping("/usersCartList")
	public String userCart(CartVO vo, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		UsersVO u = (UsersVO) session.getAttribute("user");
		vo.setUserNo(u.getUserNo());
		model.addAttribute("cartList", user.cartList(vo));
		return "users/usersCartList";
	}

	// 마이페이지 주문 내역
	@RequestMapping("/usersOrderList")
	public String orderList(OrderVO ovo, Model model, HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "5") int pageSize) {
		PageHelper.startPage(pageNum, pageSize); // 페이징처리
		HttpSession session = request.getSession();
		UsersVO vo = (UsersVO) session.getAttribute("user"); // 로그인한 계정(VO) 컨트롤러에서 불러오기
		ovo.setUserNo(vo.getUserNo());
		List<OrderVO> list = user.orderList(ovo);
		model.addAttribute("pageInfo", PageInfo.of(list));
		return "users/usersOrderList";
	}

	// 반품 신청 폼 호출
	@RequestMapping("/usersCancelForm")
	public String cancelForm(HttpServletRequest request, Model model, OrderVO ovo) {
		HttpSession session = request.getSession();
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		ovo = state.orderCanList(ovo);
		model.addAttribute("ovo", ovo);
		return "users/usersCancelForm";
	}

	// 반품 신청 처리
	@PostMapping("/usersCancel") // cancelInsert
	public String cancelInsert(OrderVO ovo, StateVO svo) {
		// state 테이블에 등록
		System.out.println("???????????" + svo.getOrderNo());
		System.out.println("???????????" + svo.getCancelDetail());
		
		state.cancelInsert(svo);
		//int orderNo = ovo.getOrderNo();
		
		// 주문 배송 상태를 업데이트 (교환 5번 취소 4번)
		// 주문 내역 삭제
		//int orderNo2 = ovo.getOrderNo();
		//ovo.setOrderNo(orderNo2);
		//order.deleteOrder(ovo);
		
		return "redirect:/usersOrderList";
	}
	
//	@PostMapping("/usersCancel")
//	public String userCancel(HttpServletRequest request, Model model, OrderVO ovo, StateVO svo) {
//		HttpSession session = request.getSession();
//		UsersVO uvo = (UsersVO) session.getAttribute("user");
//		ovo.setOrderNo(svo.getOrderNo());
//		model.addAttribute("cancelList", user.orderCanList(ovo));
//		user.cancelInsert(svo);
//		return "redirect:/usersOrderList";
//	}

	// 마이페이지 반품/교환 내역
	@RequestMapping("/usersCancelList")
	public String userCancel(OrderVO vo, Model model) {
		model.addAttribute("cancelList", state.stateSelect(vo));
		return "users/usersCancelList";
	}

	// 클래스 수강내역
	@RequestMapping("/usersClassList")
	public String userClass(ClassReserveVO crvo, Model model, HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		HttpSession session = request.getSession();
		UsersVO vo = (UsersVO) session.getAttribute("user");
		crvo.setUserNo(vo.getUserNo());
		List<ClassVO> list = user.classList(crvo);
		model.addAttribute("pageInfo", PageInfo.of(list));
		return "users/usersClassList";
	}

	// 작성댓글 조회
	@RequestMapping("/usersCommentList")
	public String userComment(Model model, HttpServletRequest request, CommentVO cvo,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		HttpSession session = request.getSession();
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		cvo.setUserNo(uvo.getUserNo());
		model.addAttribute("pageInfo", PageInfo.of(user.commentList(cvo)));
		return "users/usersCommentList";
	}

	// 작성글 조회
	@RequestMapping("/usersBoardList")
	public String userBoard(Model model, HttpServletRequest request, BoardVO bvo,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		HttpSession session = request.getSession();
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		bvo.setUserNo(uvo.getUserNo());
		model.addAttribute("pageInfo", PageInfo.of(user.boardList(bvo)));
		return "users/usersBoardList";
	}

	// 스크랩 전체 조회
	@RequestMapping("/usersScrapList")
	public String userScrap(Model model, HttpServletRequest request, ScrapVO svo,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		HttpSession session = request.getSession();
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		svo.setUserNo(uvo.getUserNo());
		model.addAttribute("pageInfo", PageInfo.of(user.scrapList(svo)));
		return "users/usersScrapList";
	}

	// 위시리스트 조회
	@RequestMapping("/usersWishList")
	public String userLike(ScrapVO svo, Model model, HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "8") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		HttpSession session = request.getSession();
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		svo.setUserNo(uvo.getUserNo());
		model.addAttribute("pageInfo", PageInfo.of(user.likeList(svo)));
		return "users/usersWishList";
	}

	// 전체 일정 조회
	@RequestMapping("/calendarSelectList")
	public String calendarSelectList() {
		return "calendar/calendarSelectList";
	}

	// 일정 상세 조회
	@RequestMapping("/calendar/calendarSelect")
	public String calendarSelect(CalendarVO vo, Model model) {
		model.addAttribute("cal", cal.calendarSelect(vo));
		return "calendar/calendarSelect";
	}

	// 일정 등록 처리
	@PostMapping("/users/calendarInsert")
	public String calendarInsert(CalendarVO vo) {
		return "redirect:calendar/calendarSelect";
	}

	// 반려동물 전체 리스트
	@RequestMapping("/pet/petSelectList")
	public String petSelectList(HttpServletRequest request, Model model, PetVO pvo,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "2") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		HttpSession session = request.getSession();
		UsersVO vo = (UsersVO) session.getAttribute("user");
		pvo.setUserNo(vo.getUserNo());
		List<PetVO> pl = pet.petSelectList(pvo);
		model.addAttribute("pageInfo", PageInfo.of(pl));
		return "pet/petSelectList";
	}

	// 반려동물 단건 조회
	@RequestMapping("/pet/petSelect")
	public String petSelect(HttpServletRequest request, PetVO pvo, Model model) {
		HttpSession session = request.getSession();
		UsersVO vo = (UsersVO) session.getAttribute("user");
		pvo.setUserNo(vo.getUserNo());
		model.addAttribute("petList", pet.petSelect(pvo));
		return "pet/petSelect";
	}

	// 반려동물 정보 수정 폼 호출
	@RequestMapping("/pet/petUpdateForm")
	public String petUpdateForm(HttpServletRequest request, PetVO pvo, Model model) {
		HttpSession session = request.getSession();
		UsersVO vo = (UsersVO) session.getAttribute("user");
		pvo.setUserNo(vo.getUserNo());
		model.addAttribute("petList", pet.petSelect(pvo));
		return "pet/petUpdateForm";
	}

	// 반려동물 정보 수정 처리
	@PostMapping("/pet/petUpdate")
	public String petUpdate(PetVO pvo, HttpServletRequest request) {
		HttpSession session = request.getSession();
		UsersVO vo = (UsersVO) session.getAttribute("user");
		pvo.setUserNo(vo.getUserNo());
		pet.petUpdate(pvo);
		pvo = pet.petSelect(pvo);
		session.setAttribute("pet", pvo);
		return "redirect:/pet/petSelectList";
	}

	// 반려동물 정보 등록 폼 호출
	@RequestMapping("/pet/petInsertForm")
	public String perInsertForm() {
		return "pet/petInsertForm";
	}

	// 반려동물 정보 등록 처리
	@PostMapping("/pet/petInsert")
	public String petInsert(Model model, HttpServletRequest request, PetVO pvo,
			@RequestPart(value = "file", required = false) MultipartFile file)
			throws IllegalStateException, IOException {
		HttpSession session = request.getSession();
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		pvo.setUserNo(uvo.getUserNo());

		model.addAttribute("petList", pet.petSelect(pvo));

		// file UpLoad 처리해야함.
		String saveFolder = (""); // 저장할 공간 변수 명
		System.out.println(saveFolder);
		File sfile = new File(saveFolder);// 물리적 저장할 위치
		String oFileName = file.getOriginalFilename();// 넘어온 파일의 이름.원래파일네임
		if (!oFileName.isEmpty()) {

			// 파일명 충돌방지를 위한 별명 만듦
			String sFileName = UUID.randomUUID().toString() + oFileName.substring(oFileName.lastIndexOf(".")); // 파일확장자찾는것,
																												// 랜덤파일네임
			String path = fileDir + "/" + sFileName;
			file.transferTo(new File(path)); // 파일을 물리적 위치에 저장

			pvo.setAttach(oFileName);
			pvo.setAttachDir(saveFolder + "/" + sFileName);
		}

		pet.petInsert(pvo);
		return "redirect:/pet/petSelectList";
	}

}
