package com.holoyolo.app.editor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;

@Controller
public class PostController {
	
	@Autowired
	PostService postService;

	@GetMapping("/editor")
	public String test() {
		return "/user/editor/editor";
	}
	
	@GetMapping("/editor/list")
	public String postList(Model model) {
		List<PostVO> posts = postService.selectAllPost("AA2");
		model.addAttribute("posts", posts);
		return "/user/editor/editorList";
	}
	
	@GetMapping("/editor/postview")
	public String postView(@AuthenticationPrincipal PrincipalDetails principalDetails,
						   int boardId, Model model) {
		String loginId = "not found";
		
		if(principalDetails != null) {
			loginId = principalDetails.getUsername();
		}
		
		PostVO board = postService.selectPostInfo(boardId);
		model.addAttribute("board", board);
		model.addAttribute("loginId", loginId);
		return "/user/editor/postView";
	}
	
	@GetMapping("/editor/updateview")
	public String updateView(@AuthenticationPrincipal PrincipalDetails principalDetails,
							int boardId, Model model) {
		String loginId = "not found";
		
		if(principalDetails != null) {
			loginId = principalDetails.getUsername();
		}
		
		PostVO board = postService.selectPostInfo(boardId);
		model.addAttribute("board", board);
		model.addAttribute("loginId", loginId);
		return "/user/editor/postUpdate";
	}
	
	@PostMapping("/editor/update")
	@ResponseBody
	public int postUpdate(@RequestBody PostVO params) {
		return postService.updatePost(params);
	}
}
