package com.team.prj.seller.web;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import com.team.prj.admin.service.ProfitVO;
import com.team.prj.goods.service.GoodsService;
import com.team.prj.goods.service.GoodsVO;
import com.team.prj.notice.service.NoticeService;
import com.team.prj.notice.service.NoticeVO;
import com.team.prj.orders.service.OrderService;
import com.team.prj.orders.service.OrderVO;
import com.team.prj.qna.service.QnaService;
import com.team.prj.qna.service.QnaVO;
import com.team.prj.seller.service.SellerService;
import com.team.prj.seller.service.SellerVO;
import com.team.prj.state.service.StateVO;

@Controller
public class SellerController {
	@Value("${file.dir}")
	private String fileDir;
	@Autowired
	private SellerService seller;
	@Autowired
	private GoodsService goods;
	// 1010 선희 추가(알림)
	@Autowired
	private NoticeService notice;
	// 1012 선희 추가
	@Autowired
	private OrderService order;
	// 1013 선희 추가
	@Autowired
	private QnaService qna;

	// 판매자 회원 전체 조회
	@RequestMapping("/sellerSelectList")
	public String sellerSelectList(Model model) {
		model.addAttribute("sellerList", seller.sellerSelectList());
		return "seller/sellerSelectList";
	}

	// 판매자 회원(개인정보) 단건 조회
	@RequestMapping("/sellerMyPage")
	public String sellerMyPage() {
		return "seller/sellerMyPage";
	}

	// 판매자 회원(개인정보) 수정 폼 호출
	@RequestMapping("/sellerMyPageUpdForm")
	public String sellerMyPageUpdForm(HttpSession session, Model model) {
		SellerVO vo = new SellerVO();
		String id = (String) session.getAttribute("id");
		vo.setId(id);
		vo = seller.sellerMyPage(vo);
		model.addAttribute("sellerList", vo);
		return "seller/sellerMyPageUpdForm";
	}

	// 판매자 회원(개인정보) 수정
	@PostMapping("/sellerMyPageUpd")
	public String sellerUpdate(SellerVO vo, HttpSession session) {
		seller.sellerUpdate(vo);
		vo = seller.sellerMyPage(vo);
		session.setAttribute("seller", vo);
		return "redirect:/sellerMyPage";
	}

	// 판매자 회원(사업자) 단건 조회
	@RequestMapping("/sellerComList")
	public String sellerComList(Model model, HttpSession session) {
		SellerVO vo = (SellerVO) session.getAttribute("seller");
		seller.sellerMyPage(vo);
		return "seller/sellerComList";
	}

	// 판매자 정보(사업자) 수정 폼 호출
	@RequestMapping("/sellerComUpForm")
	public String sellerComUpForm(HttpSession session, Model model) {
		SellerVO vo = new SellerVO();
		String id = (String) session.getAttribute("id");
		vo.setId(id);
		vo = seller.sellerMyPage(vo);
		model.addAttribute("sellerList", vo);
		return "seller/sellerComUpForm";
	}

	// 판매자 정보(사업자) 수정 처리
	@PostMapping("/sellerComUpdate")
	public String sellerComUpdate(SellerVO vo, HttpSession session) {
		seller.sellerUpdate(vo);
		vo = seller.sellerMyPage(vo);
		session.setAttribute("seller", vo);
		return "redirect:/sellerComList";
	}

	// 판매 상품 등록 폼 호출
	@RequestMapping("/sellerGIForm")
	public String sellerGoodsInsert() {
		return "seller/sellerGIForm";
	}

	// 판매 상품 등록 처리
	@PostMapping("/sellerGI")
	public String goodsInsert(Model model, HttpSession session, GoodsVO gvo,
			@RequestPart(value = "file", required = false) MultipartFile file)
			throws IllegalStateException, IOException {
		SellerVO svo = new SellerVO();
		svo.setSellerNo(gvo.getSellerNo());

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

			gvo.setThumb(oFileName);
			//svo.setAttachDir(saveFolder + "/" + sFileName);
		}
		seller.goodsInsert(gvo);
		return "redirect:/sellerGoodsList";
	}
	
	// 판매 상품 삭제
	@RequestMapping("/deleteGoods")
	public String deleteGoods(GoodsVO vo) {
		goods.deleteGoods(vo);
		return "redirect:/sellerGoodsList";
	}

	// 판매 상품 조회
	@RequestMapping("/sellerGoodsList")
	public String sellerGoodsList(Model model, HttpSession session, GoodsVO gvo,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		SellerVO svo = (SellerVO) session.getAttribute("seller");
		gvo.setSellerNo(svo.getSellerNo());
		model.addAttribute("pageInfo", PageInfo.of(seller.goodsList(gvo)));
		return "seller/sellerGoodsList";
	}

	// 상품 상세보기
	@RequestMapping("/sellerGoodsDetail")
	public String sellerGoodsDetail(GoodsVO gvo, Model model, HttpSession session) {
		SellerVO svo = (SellerVO) session.getAttribute("seller");
		gvo.setSellerNo(svo.getSellerNo());
		model.addAttribute("goodsList", goods.goodsSelectOne(gvo));
		return "seller/sellerGoodsDetail";
	}

	// 상품 수정 폼 호출
	@RequestMapping("/sellerGUForm")
	public String sellerGUForm(GoodsVO gvo, Model model, HttpSession session) {
		SellerVO svo = (SellerVO) session.getAttribute("seller");
		gvo.setSellerNo(svo.getSellerNo());
		model.addAttribute("goodsList", goods.goodsSelectOne(gvo));
		return "seller/sellerGUForm";
	}

	// 상품 수정 처리
	@PostMapping("/sellerGoodsUpdate")
	public String sellerGoodsUpdate(GoodsVO gvo, HttpSession session) {
		SellerVO svo = (SellerVO) session.getAttribute("seller");
		gvo.setSellerNo(svo.getSellerNo());
		goods.updateGoods(gvo);
		gvo = goods.goodsSelectOne(gvo);
		session.setAttribute("goods", gvo);
		return "redirect:/sellerGoodsList";
	}

	// 배송 상품 조회
	@RequestMapping("/sellerDeliList")
	public String sellerDeliList(Model model, OrderVO ovo, HttpSession session,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		SellerVO svo = (SellerVO) session.getAttribute("seller");
		ovo.setSellerNo(svo.getSellerNo());
		model.addAttribute("pageInfo", PageInfo.of(goods.deliveryList(ovo)));
		return "seller/sellerDeliList";
	}

	// 반품/교환 상품 조회
	@RequestMapping("/sellerCanList")
	public String sellerCanList(Model model, OrderVO ovo, HttpSession session,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		SellerVO svo = (SellerVO) session.getAttribute("seller");
		ovo.setSellerNo(svo.getSellerNo());
		model.addAttribute("pageInfo", PageInfo.of(goods.sellerCancelList(ovo)));
		return "seller/sellerCanList";
	}

	// 판매완료 상품 관리 페이지
	@RequestMapping("/sellerDoneList")
	public String sellerDoneList(Model model, OrderVO ovo, HttpSession session,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		SellerVO svo = (SellerVO) session.getAttribute("seller");
		ovo.setSellerNo(svo.getSellerNo());
		model.addAttribute("pageInfo", PageInfo.of(goods.sellerDoneList(ovo)));
		return "seller/sellerDoneList";
	}

	// 배송 상태 업데이트(상품준비중)
	@PostMapping("/deliveryReadyUpdate")
	public String deliveryReadyUpdate(OrderVO ovo) {
		goods.deliveryReadyUpdate(ovo);
		return "redirect:/sellerDeliList";
	}

	// 배송 상태 업데이트(배송지시)
	@PostMapping("/deliveryUpdate")
	public String deliveryUpdate(OrderVO ovo, NoticeVO nvo) {
		// 배송 지시
		long num = (long)(Math.random()*99999999) + 10000000;
		ovo.setDeliveryNo((int) num); // 운송장번호
		goods.deliveryUpdate(ovo);
		
		// 메시지를 위한 오더 단건 조회
		List<OrderVO> ol = order.selectOrder(ovo);
		OrderVO ov = ol.get(0);
		Date msgDt = ov.getDt();
		
		// 원하는 데이터 포맷 지정
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일"); 
		// 지정한 포맷으로 변환
		String strNowDate = simpleDateFormat.format(msgDt); 
		
		// 알림 테이블에 등록 1010 선희추가
		int refNo = ovo.getOrderNo();
		String type = "3"; // 알림 상태 3번(배송지시)
		String url = "usersOrderList";
		String msgGn = ov.getGoodsName(); 
		int olSize = ol.size();
		String msg = strNowDate + "에 주문하신 " + msgGn + " 외 " + (olSize-1) + "건의 상품 배송이 시작되었습니다.";
		if(olSize == 1) {
			msg = strNowDate + "에 주문하신 " + msgGn + "의 상품 배송이 시작되었습니다.";
		}
		// System.out.println("===============" + msg + "===============");

		nvo.setRefNo(refNo);
		nvo.setType(type);
		nvo.setContent(msg);
		nvo.setUrl(url);
		
		OrderVO o = new OrderVO();
		o.setOrderNo(refNo);
		int userNo = ov.getUserNo();
		nvo.setUserNo(userNo);
		notice.noticeInsert(nvo);
		
		return "redirect:/sellerDeliList";
	}

	// 배송 상태 업데이트(배송완료)
	@PostMapping("/deliveryDone")
	public String deliveryDone(OrderVO ovo) {
		goods.deliveryDone(ovo);
		return "redirect:/sellerDeliList";
	}

	// 반품 상태 업데이트(반품접수)
	@PostMapping("/cancelUpdate")
	public String cancelUpdate(StateVO svo) {
		goods.cancelUpdate(svo);
		return "redirect:/sellerCanList";
	}

	// 교환 상태 업데이트(교환접수)
	@PostMapping("/changeUpdate")
	public String changeUpdate(StateVO svo) {
		goods.changeUpdate(svo);
		return "redirect:/sellerCanList";
	}

	// 정산 페이지 // 1005 희수 추가
	@RequestMapping("/sellerProfitList")
	public String profitPageCall(Model model, @RequestParam(required = false, defaultValue = "1") int pageNum) {
		model.addAttribute("pageNum", pageNum);
		return "seller/sellerProfitList";
	}

	// 정산 테이블
	@RequestMapping("/profitTable")
	public String profitTableCall(Model model, SellerVO svo, HttpSession session, String key, String start, String end,
			String by, @RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		if (key == null) {
			key = "2";
		}

		if (start == null || start == "") {
			start = null;
		}

		if (end == null || end == "") {
			end = null;
		}

		if (by == null || by == "") {
			by = null;
		}

		svo = (SellerVO) session.getAttribute("seller");
		svo.setSellerNo(svo.getSellerNo());
		List<ProfitVO> list = seller.sellerProfitList(svo, key, start, end, by);
		model.addAttribute("pageInfo", PageInfo.of(list));
		return "seller/profitTable";
	}

	// 문의 리스트 전체 조회
	@RequestMapping("/sellerQnaSelectList")
	public String sellerQnaSelectList(Model model, QnaVO qvo, HttpSession session,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		SellerVO svo = (SellerVO) session.getAttribute("seller");
		qvo.setSellerNo(svo.getSellerNo());
		model.addAttribute("pageInfo", PageInfo.of(seller.qnaSelectList(qvo)));
		return "seller/sellerQnaSelectList";
	}

	// 문의 상세보기
	@RequestMapping("/sellerQnaSelect")
	public String sellerQnaSelect(Model model, QnaVO qvo, HttpSession session) {
		SellerVO svo = (SellerVO) session.getAttribute("seller");
		qvo.setSellerNo(svo.getSellerNo());
		model.addAttribute("qna", seller.qnaSelect(qvo));
		return "seller/sellerQnaSelect";
	}

	// 문의 답변 폼 호출
	@RequestMapping("/sellerQnaAnswer")
	public String sellerQnaAnswer(Model model, QnaVO qvo, HttpSession session) {
		model.addAttribute("qna", seller.qnaSelect(qvo));
		return "seller/sellerQnaAnswer";
	}

	// 문의 답변 처리
	@PostMapping("/qnaAnswer")
	public String qnaAnswer(QnaVO qvo, NoticeVO nvo) {
		// 문의 답변(업데이트)
		seller.qnaAnswer(qvo);

		// 알림 테이블에 등록 1010 선희추가
		QnaVO ql = qna.selectQnaOne(qvo);
		int refNo = qvo.getQnaNo();
		int goodsNo = ql.getGoodsNo();
		String title = ql.getTitle(); // null이 뜸
		String type = "2"; // 알림 상태 2번(qna)
		String url = "goods?goodsNo=" + goodsNo;
		String msg = "'" + title + "'" + " 문의에 대한 답변이 등록되었습니다.";
		
		nvo.setUrl(url);
		nvo.setRefNo(refNo);
		nvo.setContent(msg);
		nvo.setType(type);
		
		QnaVO q = new QnaVO();
		q.setQnaNo(refNo);
		int userNo = seller.qnaSelect(q).getUserNo();
		nvo.setUserNo(userNo);
		notice.noticeInsert(nvo);
		
		return "redirect:/sellerQnaSelectList";
	}

	// 문의 답변 수정 폼 호출
	@RequestMapping("/sellerQnaAnswerUp")
	public String sellerQnaAnswerUp(Model model, QnaVO qvo, HttpSession session) {
		model.addAttribute("qna", seller.qnaSelect(qvo));
		return "seller/sellerQnaAnswerUp";
	}

	// 문의 답변 수정 처리
	@PostMapping("/qnaAnswerUp")
	public String qnaAnswerUp(QnaVO qvo) {
		seller.qnaAnswer(qvo);
		return "redirect:/sellerQnaSelectList";
	}

}