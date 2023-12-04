package com.holoyolo.app.review.mapper;

import java.util.List;

import com.holoyolo.app.clubMember.service.ClubMemberVO;
import com.holoyolo.app.review.service.ReviewVO;

public interface ReviewMapper {

	//해당 클럽의 리뷰 리스트
	public List<ReviewVO> getReviewList(int id);
	
	//내가 쓴 리뷰 체크
	public int checkMyReview(ClubMemberVO vo);
	
	//리뷰등록
	public int insertReview(ReviewVO vo);
	
	//해당 클럽의 리뷰평점
	public double avgClubStar(int cid);
}
