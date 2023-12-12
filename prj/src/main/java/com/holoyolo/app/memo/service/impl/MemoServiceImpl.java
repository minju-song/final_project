package com.holoyolo.app.memo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holoyolo.app.attachment.mapper.AttachmentMapper;
import com.holoyolo.app.attachment.service.AttachmentVO;
import com.holoyolo.app.memo.mapper.MemoMapper;
import com.holoyolo.app.memo.service.MemoService;
import com.holoyolo.app.memo.service.MemoVO;

@Service
public class MemoServiceImpl implements MemoService {

	@Autowired
	MemoMapper memoMapper;
	@Autowired
	AttachmentMapper attachmentMapper;
	
	@Override
	public List<MemoVO> getMemoList(MemoVO memoVO) {
		return memoMapper.selectMemoList(memoVO);
	}

	@Override
	public MemoVO getMemo(MemoVO memoVO) {
		return memoMapper.selectMemo(memoVO);
	}

	@Override
	public int insertMemo(MemoVO memoVO, List<AttachmentVO> imgList) {
		int result = memoMapper.insertMemo(memoVO);
		
		for(int i=0; i<imgList.size(); i++) {
			AttachmentVO vo = imgList.get(i);
			vo.setMenuType("AA7");
			vo.setPostId(memoVO.getMemoId());
			attachmentMapper.insertAttachment(imgList.get(i));
		}
		
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
	@Transactional
	public int deleteMemo(MemoVO memoVO) {
		int result = memoMapper.deleteMemo(memoVO);
		result += memoMapper.deleteMemoImage(memoVO);
		return result;
	}

	@Override
	public Map<String, Object> memoIndex(MemoVO memoVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSucceed = false;
		
		int result = memoMapper.memoIndex(memoVO);
		if(result == 1) {
			isSucceed = true;
		}
		
		map.put("result", isSucceed);
		map.put("info", memoVO);
		return map;
	}

	@Override
	public int memoUploadImg(MemoVO memoVO, List<AttachmentVO> imgList) {
		int result = 0;
		for(int i=0; i<imgList.size(); i++) {
			AttachmentVO vo = imgList.get(i);
			vo.setMenuType("AA7");
			vo.setPostId(memoVO.getMemoId());
			result += attachmentMapper.insertAttachment(imgList.get(i));
		}
		return result;
	}
	
	

}