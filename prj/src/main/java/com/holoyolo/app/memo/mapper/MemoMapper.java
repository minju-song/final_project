package com.holoyolo.app.memo.mapper;

import java.util.List;


import com.holoyolo.app.memo.service.MemoVO;

public interface MemoMapper {
	//전체조회
	public List<MemoVO> selectMemoList(MemoVO memoVO);
	
	//단건조회
	public MemoVO selectMemo(MemoVO memoVO);
	
	public int selectMemoMax(MemoVO memoVO);
	
	//등록
	public int insertMemo(MemoVO memoVO);
	
	//수정
	public int updateMemo(MemoVO memoVO);
	
	//삭제
	public int deleteMemo(MemoVO memoVO);
	
	//index 수정
	public int memoIndex(MemoVO memoVO);
	
}