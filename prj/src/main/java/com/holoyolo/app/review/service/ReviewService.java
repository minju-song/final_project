package com.holoyolo.app.review.service;

import java.util.Map;

public interface ReviewService {
	
	//클럽 리뷰페이지 이동 (파라미터
	public Map<String, Object> reviewPage(int rid, String mid);
	
	//리뷰등록
	public int insertReview(ReviewVO vo);
}
