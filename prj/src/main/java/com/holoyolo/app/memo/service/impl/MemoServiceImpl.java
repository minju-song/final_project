package com.holoyolo.app.memo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.memo.mapper.MemoMapper;
import com.holoyolo.app.memo.service.MemoService;
import com.holoyolo.app.memo.service.MemoVO;

@Service
public class MemoServiceImpl implements MemoService {

	@Autowired
	MemoMapper memoMapper;
	
	@Override
	public List<MemoVO> getMemoList(MemoVO memoVO) {
		return memoMapper.selectMemoList(memoVO);
	}

	@Override
	public MemoVO getMemo(MemoVO memoVO) {
		return memoMapper.selectMemo(memoVO);
	}

	@Override
	public int insertMemo(MemoVO memoVO) {
		int result = memoMapper.insertMemo(memoVO);
		
		if(result == 1) {
			return memoVO.getMemoId();
		}
		return -1;
	}

	@Override
	public Map<String, Object> updateMemo(MemoVO memoVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSucceed = false;
		
		int result = memoMapper.updateMemo(memoVO);
		if(result == 1) {
			isSucceed = true;
		}
		
		map.put("result", isSucceed);
		map.put("info", memoVO);
		
		return map;
	}

	@Override
	public int deleteMemo(MemoVO memoVO) {
		return memoMapper.deleteMemo(memoVO);
	}

}