package com.community.board.api;

import com.community.board.api.dto.response.CreatePostLikeResponse;
import com.community.board.api.response.DeletePostLikeResponse;
import com.community.board.api.response.PostLikeResponse;
import com.community.board.application.CreatePostLikeResult;
import com.community.board.application.PostLikeService;
import com.community.global.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/postLikes")
@RequiredArgsConstructor
public class PostLIkeController {
    private final PostLikeService postLikeService;

    // 좋아요 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostLikeResponse> checkPostLike(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable("postId") Long postId
    ){
        Long memberId = principal.memberId();

        boolean checkPostLike = postLikeService.checkPostLike(memberId, postId);

        return ResponseEntity.ok(PostLikeResponse.of(checkPostLike, "해당 게시글 좋아요"));

    }

    // 게시글 좋아요 누르기.
    @PostMapping("/{postId}")
    public ResponseEntity<CreatePostLikeResponse> createLike(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable("postId") Long postId
    ) {
        Long memberId = principal.memberId();

        CreatePostLikeResult createPostLikeResult = postLikeService.createLike(memberId, postId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CreatePostLikeResponse.from(createPostLikeResult, "좋아요 누르기 성공"));
    }


    // 게시글 취소.
    @DeleteMapping("/{postId}")
    public ResponseEntity<DeletePostLikeResponse> cancelLike(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable("postId") Long postId
    ) {
        Long memberId = principal.memberId();

        postLikeService.deleteLike(memberId, postId);

        return ResponseEntity.ok(DeletePostLikeResponse.of("좋아요 취소 성공"));
    }
}
