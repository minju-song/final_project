package com.holoyolo.app.memo.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.holoyolo.app.memo.service.MemoVO;

public interface MemoMapper {
	//전체조회
	public List<MemoVO> selectMemoList(MemoVO memoVO);
	
	//단건조회
	public MemoVO selectMemo(MemoVO memoVO);
	
	//등록
	public int insertMemo(MemoVO memoVO);
	
	//수정
	public int updateMemo(MemoVO memoVO);
	
	//삭제
	public int deleteMemo(@Param("memoId") int memoId, @Param("memberId") String memberId);
}