package com.team.prj.accomo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

import com.team.prj.accomo.service.accomoService;
import com.team.prj.accomo.service.accomoVO;

//@controller + @responseBody 합쳐진것 호출한페이지로 결과를돌려준다.
@RestController
public class accomoAjaxController {

	@Autowired
	private accomoService ajaxdao;
	
	@RequestMapping("/accomoajaxSearch")	
	public List<accomoVO> ajaxSearch(String key, @RequestParam String val){

		List<accomoVO> list = ajaxdao.accomoSearch(key, val);

		return list;
	}
	
	
}
