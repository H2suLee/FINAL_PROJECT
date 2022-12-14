package com.team.prj.users.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.team.prj.board.service.BoardVO;
import com.team.prj.calendar.service.CalendarService;
import com.team.prj.calendar.service.CalendarVO;
import com.team.prj.cart.service.CartVO;
import com.team.prj.classes.service.ClassVO;
import com.team.prj.classreserve.service.ClassReserveVO;
import com.team.prj.comment.service.CommentVO;
import com.team.prj.orders.service.OrderVO;
import com.team.prj.pet.service.PetService;
import com.team.prj.pet.service.PetVO;
import com.team.prj.scrap.service.ScrapVO;
import com.team.prj.state.service.StateService;
import com.team.prj.state.service.StateVO;
import com.team.prj.users.service.UsersService;
import com.team.prj.users.service.UsersVO;

@Controller
public class UsersController {
	@Autowired
	private UsersService user; // 개인회원
	@Autowired
	private StateService state; // 반품교환상태
	@Autowired
	private CalendarService cal; // 캘린더
	@Autowired
	private PetService pet; // 반려동물 정보

	@Value("${file.dir}")
	private String fileDir;

	// 개인 회원 조회
	@RequestMapping("/usersSelect")
	public String usersSelect() {
		return "users/usersSelect";
	}

	// 회원 정보 수정 폼 호출
	@RequestMapping("/usersUpdateForm")
	public String usersUpdateForm(Model model, HttpSession session) {
		UsersVO vo = new UsersVO();
		String id = (String) session.getAttribute("id");
		vo.setId(id);
		vo = user.usersSelect(vo);
		model.addAttribute("userList", vo);
		return "users/usersUpdateForm";
	}

	// 회원 정보 수정 처리
	@PostMapping("/usersUpdate")
	public String usersUpdate(UsersVO vo, HttpSession session,
			@RequestPart(value = "file", required = false) MultipartFile file)
			throws IllegalStateException, IOException {
		// file UpLoad 처리해야함.
		String saveFolder = (""); // 저장할 공간 변수 명
		// System.out.println(saveFolder);
		File sfile = new File(saveFolder); // 물리적 저장할 위치
		String oFileName = file.getOriginalFilename(); // 넘어온 파일의 이름.원래파일네임

		if (!oFileName.isEmpty()) {

			// 파일명 충돌방지를 위한 별명 만듦
			String sFileName = UUID.randomUUID().toString() + oFileName.substring(oFileName.lastIndexOf(".")); // 파일확장자찾는것,
																												// 랜덤파일네임
			String path = fileDir + "/" + sFileName;
			file.transferTo(new File(path)); // 파일을 물리적 위치에 저장

			vo.setAttach(oFileName);
			vo.setAttachDir(saveFolder + "/" + sFileName);
		}
		
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
		return "redirect:/index";
	}

	// 마이페이지 장바구니
	@RequestMapping("/usersCartList")
	public String userCart(CartVO vo, Model model, HttpSession session) {
		UsersVO u = (UsersVO) session.getAttribute("user");
		vo.setUserNo(u.getUserNo());
		model.addAttribute("cartList", user.cartList(vo));
		return "users/usersCartList";
	}

	// 마이페이지 주문 내역
	@RequestMapping("/usersOrderList")
	public String orderList(OrderVO ovo, Model model, HttpSession session,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "5") int pageSize) {
		PageHelper.startPage(pageNum, pageSize); // 페이징처리
		UsersVO vo = (UsersVO) session.getAttribute("user"); // 로그인한 계정(VO) 컨트롤러에서 불러오기
		ovo.setUserNo(vo.getUserNo());
		List<OrderVO> list = user.orderList(ovo);
		model.addAttribute("pageInfo", PageInfo.of(list));
		return "users/usersOrderList";
	}

	// 반품 신청 폼 호출
	@RequestMapping("/usersCancelForm")
	public String cancelForm(Model model, OrderVO ovo) {
		ovo = state.orderCanList(ovo);
		model.addAttribute("ovo", ovo);
		return "users/usersCancelForm";
	}

	// 교환 신청 폼 호출
	@RequestMapping("/usersChangeForm")
	public String changeForm(Model model, OrderVO ovo) {
		ovo = state.orderCanList(ovo);
		model.addAttribute("ovo", ovo);
		return "users/usersChangeForm";
	}

	// 반품 신청 처리
	@PostMapping("/usersCancel")
	public String cancelInsert(OrderVO ovo, StateVO svo) {
		// state 테이블에 등록
		// 주문 배송 상태를 업데이트 (취소 4번)
		state.cancelInsert(svo, ovo);
		return "redirect:/usersStateList";
	}

	// 교환 신청 처리
	@PostMapping("/usersChange")
	public String changeInsert(OrderVO ovo, StateVO svo) {
		// state 테이블에 등록
		// 주문 배송 상태를 업데이트 (교환 5번)
		state.changeInsert(svo, ovo);
		return "redirect:/usersStateList";
	}

	// 구매확정 처리
	@PostMapping("/usersGoodsConfirm")
	public String goodsConfirm(OrderVO vo) {
		state.goodsConfirm(vo);
		return "redirect:/usersOrderList";
	}

	// 마이페이지 반품/교환 내역
	@RequestMapping("/usersStateList")
	public String usersStateList(OrderVO ovo, Model model, HttpSession session,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "5") int pageSize) {
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		ovo.setUserNo(uvo.getUserNo());
		PageHelper.startPage(pageNum, pageSize);
		model.addAttribute("pageInfo", PageInfo.of(state.stateSelectList(ovo)));
		return "users/usersStateList";
	}

	// 클래스 수강내역
	@RequestMapping("/usersClassList")
	public String userClass(ClassReserveVO crvo, Model model, HttpSession session,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		UsersVO vo = (UsersVO) session.getAttribute("user");
		crvo.setUserNo(vo.getUserNo());
		List<ClassVO> list = user.classList(crvo);
		model.addAttribute("pageInfo", PageInfo.of(list));
		return "users/usersClassList";
	}

	// 작성댓글 조회
	@RequestMapping("/usersCommentList")
	public String userComment(Model model, HttpSession session, CommentVO cvo,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		cvo.setUserNo(uvo.getUserNo());
		model.addAttribute("pageInfo", PageInfo.of(user.commentList(cvo)));
		return "users/usersCommentList";
	}

	// 작성글 조회
	@RequestMapping("/usersBoardList")
	public String userBoard(Model model, HttpSession session, BoardVO bvo,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		bvo.setUserNo(uvo.getUserNo());
		model.addAttribute("pageInfo", PageInfo.of(user.boardList(bvo)));
		return "users/usersBoardList";
	}

	// 스크랩 전체 조회
	@RequestMapping("/usersScrapList")
	public String userScrap(Model model, HttpSession session, ScrapVO svo,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		svo.setUserNo(uvo.getUserNo());
		model.addAttribute("pageInfo", PageInfo.of(user.scrapList(svo)));
		return "users/usersScrapList";
	}

	// 병원 스크랩 조회
	@RequestMapping("/scrapHospital")
	public String scrapHospital(Model model, HttpSession session, ScrapVO svo,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		svo.setUserNo(uvo.getUserNo());
		model.addAttribute("pageInfo", PageInfo.of(user.hospitalScrap(svo)));
		return "users/scrapHospital";
	}

	// 장례 스크랩 조회
	@RequestMapping("/scrapFuneral")
	public String scrapFuneral(Model model, HttpSession session, ScrapVO svo,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		svo.setUserNo(uvo.getUserNo());
		model.addAttribute("pageInfo", PageInfo.of(user.funeralScrap(svo)));
		return "users/scrapFuneral";
	}

	// 숙박 스크랩 조회
	@RequestMapping("/scrapAccomo")
	public String scrapAccomo(Model model, HttpSession session, ScrapVO svo,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		svo.setUserNo(uvo.getUserNo());
		model.addAttribute("pageInfo", PageInfo.of(user.accomoScrap(svo)));
		return "users/scrapAccomo";
	}

	// 커뮤니티 스크랩 조회
	@RequestMapping("/scrapCommunity")
	public String scrapCommunity(Model model, HttpSession session, ScrapVO svo,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		svo.setUserNo(uvo.getUserNo());
		model.addAttribute("pageInfo", PageInfo.of(user.communityScrap(svo)));
		return "users/scrapCommunity";
	}

	// 위시리스트 조회
	@RequestMapping("/usersWishList")
	public String userLike(ScrapVO svo, Model model, HttpSession session,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "8") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		svo.setUserNo(uvo.getUserNo());
		model.addAttribute("pageInfo", PageInfo.of(user.likeList(svo)));
		return "users/usersWishList";
	}

	// 반려동물 전체 리스트
	@RequestMapping("/petSelectList")
	public String petSelectList(HttpSession session, Model model, PetVO pvo,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "2") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		UsersVO vo = (UsersVO) session.getAttribute("user");
		pvo.setUserNo(vo.getUserNo());
		List<PetVO> pl = pet.petSelectList(pvo);
		model.addAttribute("pageInfo", PageInfo.of(pl));
		return "pet/petSelectList";
	}

	// 반려동물 단건 조회
	@RequestMapping("/petSelect")
	public String petSelect(HttpSession session, PetVO pvo, Model model) {
		UsersVO vo = (UsersVO) session.getAttribute("user");
		pvo.setUserNo(vo.getUserNo());
		model.addAttribute("petList", pet.petSelect(pvo));
		return "pet/petSelect";
	}

	// 반려동물 정보 수정 폼 호출
	@RequestMapping("/petUpdateForm")
	public String petUpdateForm(HttpSession session, PetVO pvo, Model model) {
		UsersVO vo = (UsersVO) session.getAttribute("user");
		pvo.setUserNo(vo.getUserNo());
		model.addAttribute("petList", pet.petSelect(pvo));
		return "pet/petUpdateForm";
	}

	// 반려동물 정보 수정 처리
	@PostMapping("/petUpdate")
	public String petUpdate(PetVO pvo, HttpSession session,
			@RequestPart(value = "file", required = false) MultipartFile file)
			throws IllegalStateException, IOException {
		UsersVO vo = (UsersVO) session.getAttribute("user");
		pvo.setUserNo(vo.getUserNo());

		// file UpLoad 처리해야함.
		String saveFolder = (""); // 저장할 공간 변수 명
		// System.out.println(saveFolder);
		File sfile = new File(saveFolder); // 물리적 저장할 위치
		String oFileName = file.getOriginalFilename(); // 넘어온 파일의 이름.원래파일네임

		if (!oFileName.isEmpty()) {

			// 파일명 충돌방지를 위한 별명 만듦
			String sFileName = UUID.randomUUID().toString() + oFileName.substring(oFileName.lastIndexOf(".")); // 파일확장자찾는것,
																												// 랜덤파일네임
			String path = fileDir + "/" + sFileName;
			file.transferTo(new File(path)); // 파일을 물리적 위치에 저장

			pvo.setAttach(oFileName);
			pvo.setAttachDir(saveFolder + "/" + sFileName);
		}

		pet.petUpdate(pvo);
		pvo = pet.petSelect(pvo);
		session.setAttribute("pet", pvo);
		return "redirect:/petSelectList";
	}

	// 반려동물 정보 등록 폼 호출
	@RequestMapping("/petInsertForm")
	public String perInsertForm() {
		return "pet/petInsertForm";
	}

	// 반려동물 정보 등록 처리
	@PostMapping("/petInsert")
	public String petInsert(Model model, HttpSession session, PetVO pvo,
			@RequestPart(value = "file", required = false) MultipartFile file)
			throws IllegalStateException, IOException {
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		pvo.setUserNo(uvo.getUserNo());

		// file UpLoad 처리해야함.
		String saveFolder = (""); // 저장할 공간 변수 명
		// System.out.println(saveFolder);
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
		return "redirect:/petSelectList";
	}

	// 작성글 삭제
	@RequestMapping("/usersBoardDelete")
	public String usersBoardDelete(BoardVO bvo, HttpSession session) {
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		bvo.setUserNo(uvo.getUserNo());
		user.usersBoardDelete(bvo);
		return "redirect:/usersBoardList";
	}

	// 작성댓글 삭제
	@RequestMapping("/usersCommentDelete")
	public String usersCommentDelete(CommentVO cvo, HttpSession session) {
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		cvo.setUserNo(uvo.getUserNo());
		user.usersCommentDelete(cvo);
		return "redirect:/usersCommentList";
	}

	// 스크랩 삭제
	@RequestMapping("/usersScrapDelete")
	public String usersScrapDelete(ScrapVO svo, HttpSession session) {
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		svo.setUserNo(uvo.getUserNo());
		user.usersScrapDelete(svo);
		return "redirect:/usersScrapList";
	}

	// 숙박 스크랩 삭제
	@RequestMapping("/accomoScrapDelete")
	public String accomoScrapDelete(ScrapVO svo, HttpSession session) {
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		svo.setUserNo(uvo.getUserNo());
		user.usersScrapDelete(svo);
		return "redirect:/scrapAccomo";
	}

	// 커뮤니티 스크랩 삭제
	@RequestMapping("/commuScrapDelete")
	public String commuScrapDelete(ScrapVO svo, HttpSession session) {
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		svo.setUserNo(uvo.getUserNo());
		user.usersScrapDelete(svo);
		return "redirect:/scrapCommunity";
	}

	// 장례 스크랩 삭제
	@RequestMapping("/funeralScrapDelete")
	public String funeralScrapDelete(ScrapVO svo, HttpSession session) {
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		svo.setUserNo(uvo.getUserNo());
		user.usersScrapDelete(svo);
		return "redirect:/scrapFuneral";
	}

	// 병원 스크랩 삭제
	@RequestMapping("/hospitalScrapDelete")
	public String hospitalScrapDelete(ScrapVO svo, HttpSession session) {
		UsersVO uvo = (UsersVO) session.getAttribute("user");
		svo.setUserNo(uvo.getUserNo());
		user.usersScrapDelete(svo);
		return "redirect:/scrapHospital";
	}

	// 이지지 띄우기
	@GetMapping("/displays")
	public ResponseEntity<byte[]> getImage(String fileName){
		//File file = new File("C:\\Users\\admin\\git\\FINAL_PROJECT\\TogetherPet\\src\\main\\resources\\Temp\\" + fileName);

		//테스트
		File file = new File("/home/Temp", fileName);
		ResponseEntity<byte[]> result = null;
		
		try {
			MultiValueMap<String,String> header = new LinkedMultiValueMap<String, String>();
			header.add("Content-type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
