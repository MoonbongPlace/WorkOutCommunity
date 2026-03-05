package com.community.comment.application;

import com.community.board.domain.model.Post;
import com.community.board.infra.persistence.PostRepositoryAdapter;
import com.community.comment.api.dto.request.CreateCommentRequest;
import com.community.comment.application.dto.CreateCommentResult;
import com.community.comment.application.dto.DeleteCommentResult;
import com.community.comment.application.dto.CommentDetailResult;
import com.community.comment.application.dto.CommentListResult;
import com.community.comment.domain.model.Comment;
import com.community.comment.infra.persistance.CommentRepositoryAdapter;
import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.member.domain.model.Member;
import com.community.member.infra.persistence.MemberRepositoryAdapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepositoryAdapter postRepositoryAdapter;
    private final CommentRepositoryAdapter commentRepositoryAdapter;
    private final MemberRepositoryAdapter memberRepositoryAdapter;

    // todo: 수정 예정.
    @Transactional(readOnly = true)
    public CommentListResult getComments(Long postId) {
        postRepositoryAdapter.findActiveById(postId)
                .orElseThrow(() -> new CommonException(ResponseCode.POST_NOT_FOUND));

        List<Comment> comments = commentRepositoryAdapter.findActiveByPostId(postId);
        List<CommentDetailResult> results = comments.stream()
                .map(c -> {
                    String memberName = memberRepositoryAdapter.findById(c.getMemberId())
                            .map(Member::getMemberName)
                            .orElse("알 수 없음");
                    return CommentDetailResult.from(c, memberName);
                })
                .toList();

        return new CommentListResult(results);
    }

    @Transactional
    public CreateCommentResult create(CreateCommentRequest request, Long memberId, Long postId) {
        Post post = postRepositoryAdapter.findActiveById(postId)
                .orElseThrow(()-> new CommonException(ResponseCode.POST_NOT_FOUND));
        // post 관련 refactor 및 fix 사항
//        if(!postRepositoryAdapter.existsById(postId)){
//            throw new CommonException(ResponseCode.POST_NOT_FOUND);
//        }

        Comment comment = Comment.fromRequest(request, memberId, postId);

        Comment saved = commentRepositoryAdapter.save(comment);

        return CreateCommentResult.from(saved);
    }

    @Transactional
    public DeleteCommentResult delete(Long memberId, Long postId, Long commentId) {

        // 댓글 삭제 시 해당 게시글이 존재하는지, 댓글 작성자 본인인지 확인 절차 추가
        Post post = postRepositoryAdapter.findActiveById(postId)
                .orElseThrow(()-> new CommonException(ResponseCode.POST_NOT_FOUND));
        Member member = memberRepositoryAdapter.findById(memberId)
                .orElseThrow(()-> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        Comment comment = commentRepositoryAdapter.findById(commentId)
                .orElseThrow(() -> new CommonException(ResponseCode.COMMENT_NOT_FOUND));

        if (comment.getDeletedAt() != null) {
            return DeleteCommentResult.from(comment);
        }

        comment.setDeletedAt(OffsetDateTime.now());
        Comment saved = commentRepositoryAdapter.save(comment);

        return DeleteCommentResult.from(saved);
    }
}
