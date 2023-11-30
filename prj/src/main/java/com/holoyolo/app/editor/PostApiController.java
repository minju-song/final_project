package com.holoyolo.app.editor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostApiController {

	@Autowired
    private final PostService postService;
	
    // 게시글 저장
    @PostMapping
    public int insertPost(@RequestBody final PostVO params) {
        return postService.insertPost(params);
    }

    // 게시글 상세정보 조회
    @GetMapping("/{boardId}")
    public PostVO selectPostInfo(@PathVariable final int boardId) {
        return postService.selectPostInfo(boardId);
    }


}