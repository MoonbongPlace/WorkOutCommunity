package com.community.comment.api;

import com.community.comment.api.dto.CreateCommentRequest;
import com.community.comment.api.dto.CreateCommentResponse;
import com.community.comment.api.dto.DeleteCommentResponse;
import com.community.comment.application.CommentService;
import com.community.comment.application.CreateCommentResult;
import com.community.comment.application.DeleteCommentResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        @RequestBody @Valid final CreateCommentRequest request
    ){
        // memberId Principal 에서 꺼내기
        CreateCommentResult createdComment = commentService.create(request, 0L, postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CreateCommentResponse.from(createdComment, "댓글 작성 성공"));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<DeleteCommentResponse> deleteComment(
            @PathVariable(name="commentId") final Long commentId,
            @PathVariable(name="postId") final Long postId
    ){
        Long memberId = 0L;
        DeleteCommentResult deletedComment = commentService.delete(memberId, postId, commentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(DeleteCommentResponse.from(deletedComment, "댓글 삭제 성공"));
    }


}
