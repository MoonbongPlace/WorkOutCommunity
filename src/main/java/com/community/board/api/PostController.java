package com.community.board.api;

import com.community.board.api.dto.request.UpdatePostRequest;
import com.community.board.api.dto.response.DeletePostResponse;
import com.community.board.api.dto.response.UpdatePostResponse;
import com.community.board.api.dto.request.CreatePostRequest;
import com.community.board.api.dto.response.CreatePostResponse;
import com.community.board.api.dto.response.PostResponse;
import com.community.board.application.PostService;
import com.community.board.application.dto.CreatePostResult;
import com.community.board.application.dto.DeletePostResult;
import com.community.board.application.dto.DetailPostResult;
import com.community.board.application.dto.UpdatePostResult;
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
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> findPost(
            @PathVariable(name = "postId") final Long postId
    ){
        DetailPostResult detail = postService.getPostDetail(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PostResponse.get(detail,"게시글 상세 조회 성공"));
    }

    //게시글 작성
    @PostMapping
    public ResponseEntity<CreatePostResponse> createPost(
            @RequestBody @Valid final CreatePostRequest request
    ){
        CreatePostResult createdPost = postService.create(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CreatePostResponse.create(
                        createdPost,
                        "게시글 작성 완료"
                ));
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<UpdatePostResponse> updatePost(
            @PathVariable(name = "postId") final Long postId,
            @RequestBody @Valid final UpdatePostRequest request
    ){
        UpdatePostResult updatedPost = postService.update(request, postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UpdatePostResponse.update(
                        updatedPost,
                        "게시글 수정 완료"
                ));
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<DeletePostResponse> deletePost(
            @PathVariable(name = "postId") final Long postId
    ){
        DeletePostResult deletedPost = postService.delete(postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(DeletePostResponse.delete(
                        deletedPost,
                        "게시글 삭제 완료"
                ));
    }
}
