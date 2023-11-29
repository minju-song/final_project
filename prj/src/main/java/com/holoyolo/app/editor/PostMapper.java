package com.holoyolo.app.editor;

public interface PostMapper {

	// 단건조회
	public PostVO selectPostInfo(PostVO postVO);
	
	// 등록
	public int insertPost(PostVO postVO);
	
	// 수정
	public int updatePost(PostVO postVO);
	
	// 삭제
	public int deletePost(int boardId);
}
