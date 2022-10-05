package com.team.prj.seller.web;

import java.io.File;
import java.io.IOException;
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
import com.team.prj.orders.service.OrderVO;
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

			svo.setAttach(oFileName);
			svo.setAttachDir(saveFolder + "/" + sFileName);
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
	public String deliveryUpdate(OrderVO ovo) {
		goods.deliveryUpdate(ovo);
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
	public String profitPageCall(Model model, SellerVO svo, HttpSession session) {
		svo = (SellerVO) session.getAttribute("seller");
		svo.setSellerNo(svo.getSellerNo());
		List<ProfitVO> list = seller.sellerProfitList(svo);
		System.out.println("=====================================");
		System.out.println(list.toString());
		model.addAttribute("profitList", list);
		return "seller/sellerProfitList";
	}

}
