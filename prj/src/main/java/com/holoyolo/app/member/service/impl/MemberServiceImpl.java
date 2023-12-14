package com.holoyolo.app.member.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.holoyolo.app.attachment.service.AttachmentService;
import com.holoyolo.app.member.mapper.MemberMapper;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberMapper memberMapper;
	@Autowired
	AttachmentService attachmentService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Value("${file.upload.path}")
    private String uploadPath;
	
	
	@Override
	public String joinUser(MemberVO memberVO) {
		MemberVO vo =  memberMapper.checkUserPhone(memberVO); // 회원 가입 전 인증된 번호가 있는지 확인.
		
		if(vo != null) {
			log.warn("이미 가입된 회원");
			return "JoinUser";
		} else {
			String rawPassword = memberVO.getPassword();
			String encPassword = passwordEncoder.encode(rawPassword);
			memberVO.setPassword(encPassword);
			memberVO.setRole("ROLE_HA1"); //일반회원
			
			System.out.println(memberVO);
			
			int result =  memberMapper.joinUser(memberVO);
			
			if(result > 0) {
				return "Success";
			} else {
				return "Fail";
			}
		}
	}

	@Override
	public MemberVO selectUser(String username) {
		return memberMapper.selectUser(username);
	}

	@Override
	public Date selectJoinDate(String id) {
		MemberVO vo = new MemberVO();
		//회원아이디가져오기
		vo.setMemberId(id);
		vo = memberMapper.selectJoinDate(vo);
		return vo.getJoinDate();
	}
	

	// 회원 상세보기
	@Override
	public MemberVO selectMemberInfo(MemberVO memberVO) {
		return memberMapper.selectMemberInfo(memberVO);
	}
	
	// 회원 정보수정
	@Override
	public boolean updateMemberInfo(MemberVO memberVO) {
		boolean result = false;
		
		// 닉네임 변경인 경우 체크 후 업데이트
		if(memberVO.getNickname() != null) {
			String check = this.checkNickname(memberVO);
			if(check == "NOT_FOUND") {
				int cnt = memberMapper.updateMemberInfo(memberVO);
				if(cnt > 0) result = true;
			}
		}
		
		// 비밀번호 변경인 경우 암호화 진행 후 업데이트
		if(memberVO.getPassword() != null) {
			String encodePwd = passwordEncoder.encode(memberVO.getPassword());
			memberVO.setPassword(encodePwd);
			int cnt = memberMapper.updateMemberInfo(memberVO);
			if(cnt > 0) result = true;
		}
		
		// 닉네임, 비밀번호가 아닌 경우 바로 업데이트
		int cnt = memberMapper.updateMemberInfo(memberVO);
		if(cnt > 0) result = true;
		
		return result;
	}

	@Override
	public String checkMemberId(MemberVO memberVO) {
		String result = "NOT_FOUND";
		MemberVO vo = new MemberVO();
		log.warn("memberId ::: " + memberVO.getMemberId());
		
		vo = memberMapper.checkMemberId(memberVO);
		log.warn("사용가능한 아이디 ::: " + memberVO.getMemberId());
		
		if(vo != null) {
			result = "FOUND";
			log.warn("조회 결과 ::: " + result + ", " + vo.getMemberId());
		}
		
		return result;
	}

	@Override
	public String checkNickname(MemberVO memberVO) {
		String result = "NOT_FOUND";
		
		// 조회된 데이터가 있으면, 사용불가
		MemberVO vo = new MemberVO();
		vo = memberMapper.checkNickname(memberVO);
		if(vo != null) { 
			result = "FOUND";
		}
		
		return result;
	}

	@Override
	public String findMemberIdPwd(MemberVO memberVO) {
		MemberVO vo = new MemberVO();
		vo = memberMapper.findMemberIdPwd(memberVO);
		log.warn("조회 결과 ::: " + vo);
		
		if(vo != null && vo.getStopDate() == null) {
			return vo.getMemberId();
		} else {
			return "Fail";
		}
	}

	@Override
	public int updateMemberPwd(String memberId, String authNum) {
		return memberMapper.updateMemberPwd(memberId, authNum);
	}

	@Override
	public int updateMemberFailCnt(MemberVO memberVO) {
		return memberMapper.updateMemberFailCnt(memberVO);
	}

	@Override
	public int updateMemberFailCntReset(MemberVO memberVO) {
		return memberMapper.updateMemberFailCntReset(memberVO);
	}
	
	@Override
	public String uploadImage(MultipartFile file, String memberId) {
		String result = "Success";
		
		// 파일 업로드하고, DB에 저장할 파일명 리턴받기
		String imagePath = "none";
		try {
			imagePath = attachmentService.uploadImage(file, "myProfile");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 이전파일 삭제하기
		MemberVO info = memberMapper.selectUser(memberId);
		this.deleteFile(info);
		
		// DB에 반영
		MemberVO memberVO = new MemberVO();
		memberVO.setMemberId(memberId);
		memberVO.setProfileImg(imagePath);
		memberMapper.updateMemberInfo(memberVO);
		
		if(imagePath == "none") result = "Fail";
		
		return "{\"result\":\"" + result + "\"}";
	}


	/**
	 * 휴대폰 변경-이미 사용중인 번호인지 체크
	 * @param memberVO
	 * @return
	 */
	@Override
	public boolean phoneCheck(MemberVO memberVO) {
		MemberVO vo = new MemberVO();
		vo = memberMapper.checkUserPhone(memberVO);
		if(vo != null) return false;
		
		return true;
	}

	@Override
	public boolean deleteMember(MemberVO memberVO) {
		boolean bool = this.checkPassword(memberVO);
		log.warn("deleteMember()에 넘어온 checkPassword 결과 ::: " + bool);
		if(bool == true) {
			memberMapper.deleteMemberInfo(memberVO); //회원의 관련 정보 삭제
			if(memberVO.getVResult().equals("Success")) {
				return true;
			}
		}
		return false;
	}

	// 회원 전체조회
	@Override
	public List<MemberVO> selectMemberAll(MemberVO memberVO) {
		return memberMapper.selectMemberAll(memberVO);
	}
	
	// 회원 전체 수
	@Override
	public Object selectMemberCount(MemberVO memberVO) {
		return memberMapper.selectMemberCount(memberVO);
	}

	
	@Override
	public boolean checkPassword(MemberVO memberVO) {
		MemberVO vo = memberMapper.selectUser(memberVO.getMemberId());
		String encodedPassword = vo.getPassword();
		String rawPassword = memberVO.getPassword();
		boolean bool = passwordEncoder.matches(rawPassword, encodedPassword);
		log.warn("입력한 비밀번호 ::: " + rawPassword);
		log.warn("조회된 비밀번호 ::: " + encodedPassword);
		log.warn("비밀번호 검증결과 ::: " + bool);
		
		return bool;
	}

	// 어드민 홀로페이 내역
	@Override
	public List<MemberVO> getHolopayHistory(MemberVO memberVO) {
	    List<MemberVO> payHistory = memberMapper.getHolopayHistory(memberVO);
	    DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");

	    for (MemberVO item : payHistory) {
	        // 숫자가 null이 아니고 숫자 형식인 경우에만 포맷팅 수행
	        if (item.getHolopayPrice() != null || item.getPointPrice() != null) {
	            try {
	                Number payNum = decimalFormat.parse(item.getHolopayPrice());
	                Number pointNum = decimalFormat.parse(item.getPointPrice());
	                item.setHolopayPrice(decimalFormat.format(payNum) + " 원");
	                item.setPointPrice(decimalFormat.format(pointNum) + " P");
	            } catch (ParseException e) {
	                item.setHolopayPrice("N/A");
	                item.setPointPrice("N/A");
	            }
	        } else {
	            // 숫자가 null인 경우에 대한 처리
	            item.setHolopayPrice("N/A");
	            item.setPointPrice("N/A");
	        }
	    }

	    return payHistory;
	}


	
	@Override
	public int deleteFile(MemberVO memberVO) {
		File file = new File(uploadPath + "/" + memberVO.getProfileImg());
		
		if(file.exists()) {
			file.delete();
		} else {
			System.out.println("기존 회원정보 ::: " + memberVO);
		}
		return 0;
  }

	@Override
	public MemberVO findById(String memberId) {
		return memberMapper.findById(memberId);
	}
	
	@Override
	public MemberVO addMonth(MemberVO memberVO) {
		return memberMapper.addMonth(memberVO);
	}
}
