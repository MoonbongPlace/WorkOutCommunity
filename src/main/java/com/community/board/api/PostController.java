package com.community.board.api;

import com.community.board.api.dto.request.UpdatePostRequest;
import com.community.board.api.dto.response.*;
import com.community.board.api.dto.request.CreatePostRequest;
import com.community.board.application.dto.PostListResult;
import com.community.board.application.PostService;
import com.community.board.application.dto.CreatePostResult;
import com.community.board.application.dto.DeletePostResult;
import com.community.board.application.dto.DetailPostResult;
import com.community.board.application.dto.UpdatePostResult;
import com.community.board.domain.model.Post;
import com.community.board.infra.persistence.PostRepositoryAdapter;
import com.community.global.CommonException;
import com.community.global.CustomUserPrincipal;
import com.community.global.ResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final PostRepositoryAdapter postRepositoryAdapter;

    // 게시글 리스트 조회
    @GetMapping
    public ResponseEntity<PostListResponse> listPost(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        PostListResult postListResult = postService.getPostList(pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PostListResponse.from(postListResult, "게시글 리스트 조회 성공"));
    }

    // 특정 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> findPost(
            @PathVariable(name = "postId") final Long postId
    ) {
        DetailPostResult detail = postService.getPostDetail(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PostResponse.get(detail, "게시글 상세 조회 성공"));
    }

    //게시글 작성
    @PostMapping
    public ResponseEntity<CreatePostResponse> createPost(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody @Valid final CreatePostRequest request
    ) {
        Long memberId = principal.memberId();
        CreatePostResult createdPost = postService.create(memberId, request);

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
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable(name = "postId") final Long postId,
            @RequestBody @Valid final UpdatePostRequest request
    ) {
        Long memberId = principal.memberId();
        Post post = postRepositoryAdapter.findById(postId)
                .orElseThrow(() -> new CommonException(ResponseCode.POST_NOT_FOUND));

        if (!post.getMemberId().equals(memberId)) {
            throw new CommonException(ResponseCode.INVALID_MEMBER);
        }
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
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable(name = "postId") final Long postId
    ) {
        Long memberId = principal.memberId();
        Post post = postRepositoryAdapter.findById(postId)
                .orElseThrow(() -> new CommonException(ResponseCode.POST_NOT_FOUND));

        if (!post.getMemberId().equals(memberId)) {
            throw new CommonException(ResponseCode.INVALID_MEMBER);
        }

        DeletePostResult deletedPost = postService.delete(postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(DeletePostResponse.delete(
                        deletedPost,
                        "게시글 삭제 완료"
                ));
    }
}
