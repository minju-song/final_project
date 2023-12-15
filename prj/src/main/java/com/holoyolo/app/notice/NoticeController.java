package com.holoyolo.app.notice;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.holoyolo.app.attachment.service.AttachmentService;
import com.holoyolo.app.attachment.service.AttachmentVO;
import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.board.service.BoardService;
import com.holoyolo.app.board.service.BoardVO;

@Controller
public class NoticeController {

	@Autowired
	BoardService boardService;

	@Autowired
	AttachmentService attachmentService;

	// 공지사항 리스트 페이지
	@GetMapping("/cs/help/notice")
	public String noticeList(Model mo) {
		mo.addAttribute("boardType", "공지사항");
		mo.addAttribute("menu", "cs");
		return "user/cs/noticeList";
	}

//공지사항 등록 페이지 
	@GetMapping("/cs/help/notice/Insert")
	public String insertNoticePage(Model mo, BoardVO boardVO) {
		mo.addAttribute("boardType", "공지사항");
		mo.addAttribute("menu", "cs");
		return "user/cs/noticeinsert";
	}

	// 등록
	@PostMapping("/notice/insert")
	public String noticeInsertProc(@AuthenticationPrincipal PrincipalDetails principalDetails, BoardVO boardVO,
			@RequestParam("imageFiles") MultipartFile[] imageFiles,
			@RequestParam("attachmentFiles") MultipartFile[] attachmentFiles) {

		List<AttachmentVO> imgList = attachmentService.uploadFiles(imageFiles, "notice");
		List<AttachmentVO> attachList = attachmentService.uploadFiles(attachmentFiles, "noticeAttach");
		boardVO.setMemberId(principalDetails.getUsername());
		boardService.insertNotice(boardVO, imgList, attachList);
		return "redirect:/cs/help/notice";
	}

	// 조회

	// 상세보기
	@GetMapping("/cs/help/notice/view")
	public String boardInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, int boardId, Model mo) {
		AttachmentVO attachmentVO = new AttachmentVO();

		String loginId = "not found";
		if (principalDetails != null) {
			loginId = principalDetails.getUsername();
		}
		boardService.addView(boardId);

		BoardVO vo = boardService.selectBoard(boardId);
		attachmentVO.setPostId(boardId);
		attachmentVO.setMenuType("AA6");
		Map<String, List<AttachmentVO>> returnMap = attachmentService.getCSAttachmentList(attachmentVO);

		System.out.println(returnMap);
		mo.addAttribute("menu", "cs");
		mo.addAttribute("boardVO", vo);
		mo.addAttribute("loginId", loginId);
		mo.addAttribute("noticeImg", returnMap.get("imgList"));
		mo.addAttribute("noticeAttach", returnMap.get("attachList"));

		return "user/cs/noticeview";

	}

//공지 수정
	@GetMapping("/cs/help/notice/update")
	public String updateView(@AuthenticationPrincipal PrincipalDetails principalDetails, AttachmentVO attachmentVO,
			int boardId, Model mo) {
		String loginId = "not found";
		if (principalDetails != null) {
			loginId = principalDetails.getUsername();
		}
		attachmentVO.setPostId(boardId);
		attachmentVO.setMenuType("AA6");
		Map<String, List<AttachmentVO>> returnMap = attachmentService.getCSAttachmentList(attachmentVO);
		mo.addAttribute("menu", "cs");
		mo.addAttribute("board", boardService.selectBoard(boardId));
		mo.addAttribute("noticeImg", returnMap.get("imgList"));
		mo.addAttribute("noticeAttach", returnMap.get("attachList"));

		return "user/cs/noticeUpdate";
	}

	/*
	 * 
	 * 데이터 로드 등록된 이미지/첨부 삭제 새로 테이블 등록 board 테이블은 수정
	 */
	// 수정등록
	@PostMapping("/notice/update")
	public String noticeUpdateProcess(@AuthenticationPrincipal PrincipalDetails principalDetails, BoardVO boardVO,
			@RequestParam("imageFiles") MultipartFile[] imageFiles,
			@RequestParam("attachmentFiles") MultipartFile[] attachmentFiles) {
		List<AttachmentVO> imgList = attachmentService.uploadFiles(imageFiles, "notice");
		List<AttachmentVO> attachList = attachmentService.uploadFiles(attachmentFiles, "noticeAttach");
		boardVO.setMemberId(principalDetails.getUsername());
		boardService.updateNotice(boardVO, imgList, attachList);
		return "redirect:/cs/help/notice";
	}

	@Value("${file.upload.path}")
	private String uploadPath;

	// 다운로드 처리
	@GetMapping("/download")
	public ResponseEntity<Resource> downloadFile(@RequestParam String saveFile) {
		// 파일의 경로
		Path filePath = Paths.get(uploadPath, saveFile).normalize();
		Resource resource;
		try {
			resource = new UrlResource(filePath.toUri());
			if (resource.exists() && resource.isReadable()) {

				String filename = resource.getFilename();
				String encodedFilename = null;
				try {
					encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace(); // 예외 처리 필요
				}
				// 파일 다운로드를 위한 HTTP 응답 헤더 설정
				HttpHeaders headers = new HttpHeaders();
				headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFilename);
				return ResponseEntity.ok().headers(headers).body(resource);
			} else {
				// 파일이 존재하지 않거나 읽을 수 없는 경우 404 에러 반환
				return ResponseEntity.notFound().build();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build(); // 예외 발생 시 400 Bad Request 반환
		}
	}

	@PostMapping("/deleteNotice")
	@ResponseBody
	public Map<String, Object> deleteNotice(@RequestBody BoardVO reqboardVO) {

		// 본문삭제
		int boardResult = boardService.deleteBoard(reqboardVO);
		// 첨부및 이미지 삭제
		reqboardVO.setMenuType("AA6");
		int attResult = attachmentService.deletePostAttachment(reqboardVO);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (boardResult == 1 || attResult == 1) {
			resultMap.put("resultMsg", "공지가 삭제되었습니다");
			resultMap.put("resultCode", "1");
		} else {
			resultMap.put("resultMsg", "err");
			resultMap.put("resultCode", "0");
		}
		return resultMap;
	}

	// cs 첨부파일 삭제

	// 삭제
	@GetMapping("/member/attachment/delete")
	@ResponseBody
	public void attachmentDelete(@RequestParam(name = "saveFile") String originname,
			@RequestParam(name = "postId") int boardId, @RequestParam(name = "menuType") String menuType) {
		
		attachmentService.deleteCSAttachment(boardId, menuType, originname);
	}
}
