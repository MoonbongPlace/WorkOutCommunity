package com.community.board.api;

import com.community.board.api.dto.response.CreatePostLikeResponse;
import com.community.board.application.CreatePostLikeResult;
import com.community.board.application.postLikeService;
import com.community.global.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/postLikes")
@RequiredArgsConstructor
public class PostLIkeController {
    private final postLikeService postLikeService;

    // 게시글 좋아요 누르기.
    @PostMapping("/{postId}")
    public ResponseEntity<CreatePostLikeResponse> createLIke(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable("postId") Long postId
            ){
        Long memberId = principal.memberId();

        CreatePostLikeResult createPostLikeResult = postLikeService.createLike(memberId, postId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CreatePostLikeResponse.from(createPostLikeResult, "좋아요 누르기 성공"));
    }
}
