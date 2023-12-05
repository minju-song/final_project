package com.holoyolo.app.review.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.club.mapper.ClubMapper;
import com.holoyolo.app.club.service.ClubVO;
import com.holoyolo.app.clubMember.service.ClubMemberVO;
import com.holoyolo.app.review.mapper.ReviewMapper;
import com.holoyolo.app.review.service.ReviewService;
import com.holoyolo.app.review.service.ReviewVO;

@Service
public class ReviewServiceImpl implements ReviewService{
	
	@Autowired
	ReviewMapper reviewMapper;
	
	@Autowired
	ClubMapper clubMapper;

	@Override
	public Map<String, Object> reviewPage(int cid, String mid) {
		Map<String, Object> map = new HashMap<>();
		
		//해당 리뷰
		List<ReviewVO> reviewList = reviewMapper.getReviewList(cid);
		
		//해당 클럽
		ClubVO vo = new ClubVO();
		vo.setClubId(cid);
		vo = clubMapper.getClub(vo);
		
		//내가 쓴 리뷰 체크
		ClubMemberVO cmvo = new ClubMemberVO();
		cmvo.setClubId(cid);
		cmvo.setMemberId(mid);
		
		int ck = reviewMapper.checkMyReview(cmvo);
		//내가 쓴 리뷰 있으면 true 없으면 false
		if (ck > 0) map.put("myreview", true);
		else map.put("myreview", false);
		
		//클럽 리뷰평점
		double avgstar = reviewMapper.avgClubStar(cid);
		
		map.put("avgstar", avgstar);
		map.put("reviews", reviewList);
		map.put("club", vo);	
		
		return map;
	}

	@Override
	public int insertReview(ReviewVO vo) {
		return reviewMapper.insertReview(vo);
	}

	@Override
	public int updateReview(ReviewVO vo) {
		return reviewMapper.updateReview(vo);
	}

	@Override
	public int deleteReview(ReviewVO vo) {
		return reviewMapper.deleteReview(vo);
	}
}
