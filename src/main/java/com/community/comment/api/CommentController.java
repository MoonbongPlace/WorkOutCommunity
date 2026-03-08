package com.community.comment.api;

import com.community.comment.api.dto.request.CreateCommentRequest;
import com.community.comment.api.dto.response.CreateCommentResponse;
import com.community.comment.api.dto.response.DeleteCommentResponse;
import com.community.comment.api.dto.response.CommentListResponse;
import com.community.comment.api.dto.response.ReadCountCommentResponse;
import com.community.comment.application.CommentService;
import com.community.comment.application.dto.CreateCommentResult;
import com.community.comment.application.dto.DeleteCommentResult;
import com.community.comment.application.dto.CommentListResult;
import com.community.comment.application.dto.ReadCountCommentResult;
import com.community.global.CustomUserPrincipal;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment", description = "댓글 API")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/posts/{postId}/comments")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 목록 조회
    @GetMapping
    public ResponseEntity<CommentListResponse> getComments(
            @PathVariable(name = "postId") final Long postId
    ) {
        CommentListResult commentListResult = commentService.getComments(postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommentListResponse.from(commentListResult, "댓글 조회 성공"));
    }

    // 댓글 작성
    @PostMapping
    public ResponseEntity<CreateCommentResponse> leaveComment(
            @PathVariable(name="postId") final Long postId,
            @RequestBody @Valid final CreateCommentRequest request,
            @AuthenticationPrincipal CustomUserPrincipal principal
            ){
        Long memberId = principal.memberId();
        CreateCommentResult createdComment = commentService.create(request, memberId, postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CreateCommentResponse.from(createdComment, "댓글 작성 성공"));
    }

    // 댓글 카운트
    @GetMapping("/commentCount")
    public ResponseEntity<ReadCountCommentResponse> readCount(
            @PathVariable(name="postId") final Long postId
    ){
        int readCountCommentResult = commentService.readCountComment(postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ReadCountCommentResponse.from(readCountCommentResult, "댓글 갯수 조회 성공"));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<DeleteCommentResponse> deleteComment(
            @PathVariable(name="commentId") final Long commentId,
            @PathVariable(name="postId") final Long postId,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ){
        Long memberId = principal.memberId();
        DeleteCommentResult deletedComment = commentService.delete(memberId, postId, commentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(DeleteCommentResponse.from(deletedComment, "댓글 삭제 성공"));
    }


}
