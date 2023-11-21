package com.holoyolo.app.boardLike.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.boardLike.mapper.BoardLikeMapper;
import com.holoyolo.app.boardLike.service.BoardLikeService;

@Service
public class BoardLikeServiceImpl implements BoardLikeService{
	
	@Autowired
	BoardLikeMapper boardLikeMapper;

}
