package com.team.prj.funeral.mapper;

import java.util.List;

import com.team.prj.funeral.service.FuneralVO;
import com.team.prj.review.service.ReviewVO;
import com.team.prj.scrap.service.ScrapVO;


public interface FuneralMapper {

	List<FuneralVO> findAll();//장례목록
	
	FuneralVO funeralSelect(FuneralVO vo); //장례 단건조회
	int funeralInsert(FuneralVO vo);// 장례 입력
	int funeralUpdate(FuneralVO vo);//장례수정
	int funeralDelete(FuneralVO vo);//장례 삭제
	
	
		//소현스크랩기능
		int scrapInsert(ScrapVO vo);//20221007소현추가
		
		//소현 리뷰기능 20221008
		List<ReviewVO> reviewSelectList(ReviewVO vo);
		
		ReviewVO reviewSelect(ReviewVO vo);
		
		int reviewInsert(ReviewVO vo);
		
		int reviewUpdate(ReviewVO vo);
		
		int reviewDelete(ReviewVO vo);
	
}
