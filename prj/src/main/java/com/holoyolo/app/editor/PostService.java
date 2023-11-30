package com.holoyolo.app.editor;

import java.util.List;
import java.util.Map;

public interface PostService {
	
	// 전체조회
	public List<PostVO> selectAllPost(String menyType);
	
	// 단건조회
	public PostVO selectPostInfo(int boardId);
	
	// 등록
	public int insertPost(PostVO postVO);
	
	// 수정
	public int updatePost(PostVO postVO);
	
	// 삭제
	public int deletePost(int boardId);

}
