package com.team.prj.review.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team.prj.review.mapper.ReviewMapper;

@Service
public class ReviewServiceImpl implements ReviewService {
	@Autowired
	private ReviewMapper map;
	


	@Override
	public int reviewInsert(ReviewVO vo) {
		// TODO Auto-generated method stub
		return map.reviewInsert(vo);
	}

	@Override
	public int reviewUpdate(ReviewVO vo) {
		// TODO Auto-generated method stub
		return map.reviewUpdate(vo);
	}

	@Override
	public int reviewDelete(ReviewVO vo) {
		// TODO Auto-generated method stub
		return map.reviewDelete(vo);
	}

	@Override
	public List<ReviewVO> reviewSelectList(ReviewVO vo) {
		// TODO Auto-generated method stub
		return map.reviewSelectList();
	}

}
