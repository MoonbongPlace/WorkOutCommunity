package com.community.board.api;

import com.community.board.api.dto.CreatePostRequest;
import com.community.board.api.dto.PostDetailDTO;
import com.community.board.api.dto.PostResponse;
import com.community.board.application.PostService;
import com.community.board.domain.model.Post;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    // 특정 게시글 상세 조회
    @PostMapping("/{postId}")
    public ResponseEntity<PostResponse> findPost(
            @PathVariable(name = "postId") final Long postId
    ){
        PostDetailDTO postDetailDTO = postService.getPostDetail(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PostResponse.get(postDetailDTO,"게시글 상세 조회 성공"));
    }

    // 게시글 작성
//    @PostMapping
//    public ResponseEntity<PostResponse> createPost(
//            @RequestBody @Valid final CreatePostRequest request
//    ){
//        Post createdPost = postService.create(request);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(PostResponse.create(
//                        createdPost,
//                        "게시글 작성 완료"
//                ));
//    }


}
