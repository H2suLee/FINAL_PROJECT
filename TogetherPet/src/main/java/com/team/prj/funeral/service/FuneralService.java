package com.team.prj.funeral.service;

import java.util.List;

import com.team.prj.review.service.ReviewVO;
import com.team.prj.scrap.service.ScrapVO;



public interface FuneralService {
	List<FuneralVO> findAll();// 장례목록

	FuneralVO funeralSelect(FuneralVO vo); // 장례 단건조회

	int funeralInsert(FuneralVO vo);// 장례 입력

	int funeralUpdate(FuneralVO vo);// 장례수정

	int funeralDelete(FuneralVO vo);// 장례 삭제
	
		//소현 스크랩 기능추가 1007
		int scrapInsert(ScrapVO vo);//20221005소현추가
		
		
		//소현 리뷰기능 20221008
				
				
				ReviewVO reviewSelect(ReviewVO vo);
				
				int reviewInsert(ReviewVO vo);
				
				int reviewUpdate(ReviewVO vo);
				
				int reviewDelete(ReviewVO vo);

				List<ReviewVO> reviewSelectList(ReviewVO vo);
	
}
