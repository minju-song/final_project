package com.holoyolo.app.review.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.holoyolo.app.review.service.ReviewService;

@Controller
public class ReviewController {
	
	@Autowired
	ReviewService reviewService;
}
