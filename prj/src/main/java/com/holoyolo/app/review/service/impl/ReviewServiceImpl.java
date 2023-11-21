package com.holoyolo.app.review.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.review.mapper.ReviewMapper;

@Service
public class ReviewServiceImpl {
	
	@Autowired
	ReviewMapper reviewMapper;
}
