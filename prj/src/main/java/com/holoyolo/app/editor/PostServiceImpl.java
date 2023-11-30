package com.holoyolo.app.editor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.editor.mapper.PostMapper;

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	PostMapper postMapper;

	@Override
	public List<PostVO> selectAllPost(String menuType) {
		return postMapper.selectAllPost(menuType);
	}
	
	@Override
	public PostVO selectPostInfo(int boardId) {
		return postMapper.selectPostInfo(boardId);
	}

	@Override
	public int insertPost(PostVO postVO) {
		postMapper.insertPost(postVO);
		return postVO.getBoardId();
	}

	@Override
	public int updatePost(PostVO postVO) {
		postMapper.updatePost(postVO);
		return postVO.getBoardId();
	}

	@Override
	public int deletePost(int boardId) {
		return 0;
	}


}
