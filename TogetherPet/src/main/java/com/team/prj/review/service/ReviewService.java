package com.team.prj.review.service;

import java.util.List;

public interface ReviewService {

	List<ReviewVO> reviewSelectList(ReviewVO vo);

	int reviewInsert(ReviewVO vo);

	int reviewUpdate(ReviewVO vo);

	int reviewDelete(ReviewVO vo);
}
