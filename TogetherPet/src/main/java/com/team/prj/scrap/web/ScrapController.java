package com.team.prj.scrap.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.team.prj.classes.service.ClassService;
import com.team.prj.funeral.service.FuneralService;
import com.team.prj.hospital.service.HospitalService;
import com.team.prj.scrap.service.ScrapService;
import com.team.prj.scrap.service.ScrapVO;

@Controller
public class ScrapController {
	@Autowired
	private ScrapService scrap;
	@Autowired
	private HospitalService hospital;
	@Autowired
	private FuneralService funeral;
	@Autowired
	private ClassService classes;
	
	
	// 스크랩 전체 조회
	@GetMapping("/scrapSelectList")
	public String scrapSelectList(Model model ,String key) {
		model.addAttribute("scrapList", scrap.scrapSelectList());
		model.addAttribute("hospitalList", hospital.hospitalSelectList(key));
		return "scrap/scrapSelectList";
	}
	
	// 스크랩 단건 조회
	@GetMapping("/scrapSelect")
	public String scrapSelect(ScrapVO vo, Model model) {
		model.addAttribute("scrapList", scrap.scrapSelect(vo));
		return "scrap/scrapSelect";
	}
	
	
	
	
	
	
	
	
}
