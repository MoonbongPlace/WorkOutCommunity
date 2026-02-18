package com.community.comment.api;

import com.community.comment.api.dto.CreateCommentRequest;
import com.community.comment.api.dto.CreateCommentResponse;
import com.community.comment.api.dto.DeleteCommentResponse;
import com.community.comment.application.CommentService;
import com.community.comment.application.CreateCommentResult;
import com.community.comment.application.DeleteCommentResult;
import com.community.global.CustomUserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/posts/{postId}/comments")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
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
