package com.holoyolo.app.question.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.answer.service.AnswerService;
import com.holoyolo.app.answer.service.AnswerVO;
import com.holoyolo.app.question.mapper.QuestionMapper;
import com.holoyolo.app.question.service.QuestionService;
import com.holoyolo.app.question.service.QuestionVO;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired // 매퍼 주입
	QuestionMapper questionMapper;



	@Autowired
	AnswerService answerService;

	// 문의 전체조회
	@Override
	public List<QuestionVO> selectQuestionAll() {
		return questionMapper.selectQuestionAll();
	}
	
	// 문의 조건조회
    @Override
    public List<QuestionVO> selectQuestionTotalList() {
        return questionMapper.selectQuestionTotalList();
    }

    @Override
    public List<QuestionVO> selectQuestionPendingList() {
        return questionMapper.selectQuestionPendingList();
    }

    @Override
    public List<QuestionVO> selectQuestionCompletedList() {
        return questionMapper.selectQuestionCompletedList();
    }

	// 문의 단건조회
	@Override
	public Map<String, Object> selectQuestionInfo(QuestionVO questionVO) {
		Map<String, Object> result = new HashMap<>();

		
		// 문의 단건정보
		QuestionVO findQuestionVO = questionMapper.selectQuestionInfo(questionVO);

		// 문의유형 가공
		Map<String, Object> questionTypeInfo = questionTypeProcess(findQuestionVO);
		
		// 답변 전체조회
		List<AnswerVO> findAnswerVO = answerService.selectAnswerAll(questionVO);

	    result.put("questionInfo", findQuestionVO);
	    result.put("answerInfo", findAnswerVO);
	    result.put("questionTypeInfo", questionTypeInfo);

		return result;
	}
	
	// 문의유형 분기처리
	private Map<String, Object> questionTypeProcess(QuestionVO questionVO) {
		String getQT = questionVO.getQuestionType();
		String questionTypeText = "";
		String questionTypeColor = ""; 
		
		switch (getQT) {
		case "MA1":
			questionTypeText = "가계부";
			questionTypeColor = "red";
			break;
		case "MA2":
			questionTypeText = "중고거래";
			questionTypeColor = "orange";
			break;
		case "MA3":
			questionTypeText = "알뜰모임";
			questionTypeColor = "yellow";
			break;
		case "MA4":
			questionTypeText = "커뮤니티";
			questionTypeColor = "green";
			break;
		case "MA5":
			questionTypeText = "메모";
			questionTypeColor = "blue";
			break;
		case "MA6":
			questionTypeText = "홀로페이";
			questionTypeColor = "navy";
			break;
		case "MA7":
			questionTypeText = "포인트";
			questionTypeColor = "violet";
			break;
		case "MA8":
			questionTypeText = "기타";
			questionTypeColor = "gray";
			break;
		default:
			System.out.println("");
		}
		
		Map<String, Object> questionTypeInfo = new HashMap<>();
		questionTypeInfo.put("text", questionTypeText);
		questionTypeInfo.put("color", questionTypeColor);
		
		return questionTypeInfo;
	}

	// 문의 개수조회
    @Override
    public int selectQuestionTotalCount() {
        return questionMapper.selectQuestionTotalCount();
    }

    @Override
    public int selectQuestionPendingCount() {
        return questionMapper.selectQuestionPendingCount();
    }

    @Override
    public int selectQuestionCompletedCount() {
        return questionMapper.selectQuestionCompletedCount();
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
		int result = questionMapper.deleteQuestionInfo(questionId);

		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}
}
