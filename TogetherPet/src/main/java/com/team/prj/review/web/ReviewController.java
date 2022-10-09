package com.team.prj.review.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.team.prj.review.service.ReviewService;
import com.team.prj.review.service.ReviewVO;

@Controller
public class ReviewController {
	@Autowired
	private ReviewService service;
	
	//리뷰전체조회
	@GetMapping("reviewSelectList")
	public String reviewSelectList(ReviewVO vo, Model model) {
		model.addAttribute("reviewList",service.reviewSelectList(vo));
		return "funeral/reviewSelectList";
		
	}
	
}
