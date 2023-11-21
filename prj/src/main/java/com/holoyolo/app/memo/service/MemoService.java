package com.holoyolo.app.memo.service;

import java.util.List;
import java.util.Map;

public interface MemoService {
	//전체조회
	public List<MemoVO> getMemoList();
	
	//단건조회
	public MemoVO getMemo(MemoVO memoVO);
	
	//등록
	public int insertMemo(MemoVO memoVO);
	
	//수정
	public Map<String, Object> updateMemo(MemoVO memoVO);
	
	//삭제
	public int deleteMemo(int memoId, String memberId);
}