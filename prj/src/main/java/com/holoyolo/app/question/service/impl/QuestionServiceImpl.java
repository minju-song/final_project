package com.holoyolo.app.question.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holoyolo.app.answer.service.AnswerService;
import com.holoyolo.app.answer.service.AnswerVO;
import com.holoyolo.app.attachment.service.AttachmentService;
import com.holoyolo.app.attachment.service.AttachmentVO;
import com.holoyolo.app.question.mapper.QuestionMapper;
import com.holoyolo.app.question.service.QuestionService;
import com.holoyolo.app.question.service.QuestionVO;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired // 매퍼 주입
	QuestionMapper questionMapper;

	@Autowired
	AnswerService answerService;

	@Autowired
	AttachmentService attachmentService;

	// 문의 조건조회
	@Override
	public List<QuestionVO> selectQuestionTotalList(QuestionVO questionVO) {
		return questionMapper.selectQuestionTotalList(questionVO);
	}

	// 문의 단건조회
	@Override
	public Map<String, Object> selectQuestionInfo(QuestionVO questionVO) {
		Map<String, Object> result = new HashMap<>();
		AttachmentVO attachmentVO = new AttachmentVO();

		// 문의 단건정보
		QuestionVO findQuestionVO = questionMapper.selectQuestionInfo(questionVO);

		// 답변 전체조회
		List<AnswerVO> findAnswerVO = answerService.selectAnswerAll(questionVO);
		System.out.println(">>>>>>>>>>>>>>>>>" + findAnswerVO);

		attachmentVO.setPostId(findQuestionVO.getQuestionId());
		attachmentVO.setMenuType("AA8");

		// 첨부파일 가져오기
		Map<String, List<AttachmentVO>> returnMap = attachmentService.getCSAttachmentList(attachmentVO);

		System.out.println("모델 값>>>>>>> ::" + returnMap.get("imgList"));
		result.put("imgInfo", returnMap.get("imgList"));
		result.put("attachInfo", returnMap.get("attachList"));
		result.put("questionInfo", findQuestionVO);
		result.put("answerInfo", findAnswerVO);

		return result;
	}

	// 문의 개수조회
	@Override
	public int selectQuestionTotalCount(QuestionVO questionVO) {
		return questionMapper.selectQuestionTotalCount(questionVO);
	}

	// 문의 등록
	@Override
	public int insertQuestionInfo(QuestionVO questionVO) {
		int result = questionMapper.insertQuestionInfo(questionVO);

		if (result == 1) {
			return questionVO.getQuestionId();
		} else {
			return -1;
		}
	}

	// 문의 수정
	@Override
	public Map<String, Object> updateQuestionInfo(QuestionVO questionVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;

		int result = questionMapper.updateQuestionInfo(questionVO);
		if (result == 1) {
			isSuccessed = true;
		}

		map.put("result", isSuccessed);
		map.put("target", questionVO);

		return map;
	}

	// 문의 삭제
	@Override
	public boolean deleteQuestionInfo(int questionId) {
		boolean result;
		if (questionMapper.deleteQuestionInfo(questionId) == 1) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	// 페이징

	@Override
	public int selectTotalPagingCount(QuestionVO questionVO) {
		return questionMapper.selectTotalPagingCount(questionVO);
	}

	public Page<QuestionVO> MyQuestionList(String memberId, Pageable pageable) {
		int offset = pageable.getPageNumber() * pageable.getPageSize();
		int limit = pageable.getPageSize();

		List<QuestionVO> questionList = questionMapper.MyQuestionList(memberId, offset, limit);
		return new PageImpl<>(questionList, pageable, questionList.size());
	}

	@Override
	public int myQuestionCnt(String memberId) {
		return questionMapper.myQuestionCnt(memberId);
	}

	@Override
	@Transactional
	public int insertQuestion(QuestionVO questionVO, List<AttachmentVO> imgList, List<AttachmentVO> attachList) {

		int result = questionMapper.insertQuestionInfo(questionVO);

		if (imgList != null) {
			for (AttachmentVO vo : imgList) {
				vo.setMenuType("AA8");
				vo.setPostId(questionVO.getQuestionId());
				attachmentService.insertAttachment(vo);
			}
		}

		if (attachList != null) {
			for (AttachmentVO vo : attachList) {
				vo.setMenuType("AA8");
				vo.setPostId(questionVO.getQuestionId());
				attachmentService.insertAttachment(vo);
			}
		}
		if (result == 1) {
			return questionVO.getQuestionId();
		} else {
			return -1;
		}
	}

	@Override
	public QuestionVO selectQuestion(int questionId) {

		return questionMapper.selectQuestion(questionId);
	}

	@Override
	@Transactional
	public int updateQuestion(QuestionVO questionVO, List<AttachmentVO> imgList, List<AttachmentVO> attachList) {
		
		// 본문 내용 UPDATE
		int result = questionMapper.updateQuestion(questionVO);
		
		// 저장된 파일 목록 들고오기
		AttachmentVO vo = new AttachmentVO();
		vo.setMenuType("AA8");
		vo.setPostId(questionVO.getQuestionId());
		List<AttachmentVO> saveList = attachmentService.getAttachmentList(vo);

		// 이미지 중복체크 및 등록
		if (imgList != null) {
			// 중복체크
			for (int i=0; i<saveList.size(); i++) {
				for (int j=0; j<imgList.size(); j++) {
					if (saveList.get(i).getOriginFile().equals(imgList.get(j).getOriginFile())) {
						imgList.remove(imgList.get(j));
						break;
					}
				}
			}
			// 이미지 등록
			for (AttachmentVO avo : imgList) {
				avo.setPostId(questionVO.getQuestionId());
				avo.setMenuType("AA8");
				attachmentService.insertAttachment(avo);
			}
		}

		
		// 첨부파일 중복체크 및 등록
		if (attachList != null) {
			// 중복체크
			for (int i=0; i<saveList.size(); i++) {
				for (int j=0; j<attachList.size(); j++) {
					if (saveList.get(i).getOriginFile().equals(attachList.get(j).getOriginFile())) {
						attachList.remove(attachList.get(j));
						break;
					}
				}
			}
			// 첨부파일 등록
			for (AttachmentVO avo : attachList) {
				avo.setMenuType("AA8");
				avo.setPostId(questionVO.getQuestionId());
				attachmentService.insertAttachment(avo);
			}
		}

		if (result == 1) {
			return questionVO.getQuestionId();
		} else {
			return -1;
		}

	}

	@Override
	public List<QuestionVO> searchQuestionSurfPaged(JSONObject req) {
		String search = (String) req.get("search");
		String memberId = (String) req.get("memberId");
		QuestionVO vo = new QuestionVO();
		List<QuestionVO> resultList = new ArrayList<QuestionVO>();
		if (search == "") {
			resultList = questionMapper.searchQuestionList(vo);
		} else {
			vo.setSearch(search);
			vo.setMemberId(memberId);
			vo.setSearchOption((String) req.get("searchType"));
			resultList = questionMapper.searchQuestionList(vo);
		}
		return resultList;
	}

	@Override
	public int getTotalQuestionSurfRecords(JSONObject req) {
		QuestionVO vo = new QuestionVO();
		vo.setSearch((String) req.get("search"));
		vo.setMemberId((String) req.get("memberId"));

		return questionMapper.getTotalQuestionSurfRecords(vo);

	}

}
