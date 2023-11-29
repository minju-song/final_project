package com.holoyolo.app.editor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
	public String postView(int boardId) {
		return "/user/editor/postView";
	}
}
