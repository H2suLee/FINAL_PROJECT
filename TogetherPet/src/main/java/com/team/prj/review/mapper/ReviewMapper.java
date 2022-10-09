package com.team.prj.review.mapper;

import java.util.List;

import com.team.prj.review.service.ReviewVO;

public interface ReviewMapper {
	
	List<ReviewVO> reviewSelectList();
	
	ReviewVO reviewSelect(ReviewVO vo);
	
	int reviewInsert(ReviewVO vo);
	
	int reviewUpdate(ReviewVO vo);
	
	int reviewDelete(ReviewVO vo);

}
