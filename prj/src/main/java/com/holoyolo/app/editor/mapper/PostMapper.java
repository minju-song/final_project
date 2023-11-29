package com.holoyolo.app.editor.mapper;

import java.util.List;

import com.holoyolo.app.editor.PostVO;

public interface PostMapper {
	
	// 전체조회
	public List<PostVO> selectAllPost();

	// 단건조회
	public PostVO selectPostInfo(int boardId);
	
	// 등록
	public int insertPost(PostVO postVO);
	
	// 수정
	public int updatePost(PostVO postVO);
	
	// 삭제
	public int deletePost(int boardId);
}
