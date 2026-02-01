package com.community.comment.application;

import com.community.board.domain.model.Post;
import com.community.board.infra.persistence.PostRepositoryAdapter;
import com.community.comment.api.dto.CreateCommentRequest;
import com.community.comment.domain.model.Comment;
import com.community.comment.infra.persistance.CommentRepositoryAdapter;
import com.community.member.domain.model.Member;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepositoryAdapter postRepositoryAdapter;
    private final CommentRepositoryAdapter commentRepositoryAdapter;
    private final MemberRepositoryAdapter memberRepositoryAdapter;

    @Transactional
    public CreateCommentResult create(CreateCommentRequest request, Long memberId, Long postId) {
        Post post = postRepositoryAdapter.findById(postId).orElseThrow();

        Comment comment = new Comment(
                memberId,
                postId,
                request.getContent(),
                OffsetDateTime.now()
        );
        Comment saved = commentRepositoryAdapter.save(comment);

        return CreateCommentResult.from(saved);
    }

    @Transactional
    public DeleteCommentResult delete(Long memberId, Long postId, Long commentId) {

        // 댓글 삭제 시 해당 게시글이 존재하는지, 댓글 작성자 본인인지 확인 절차 추가
        Post post = postRepositoryAdapter.findById(postId).orElseThrow();
        Member member = memberRepositoryAdapter.findById(memberId).orElseThrow();

        Comment comment = commentRepositoryAdapter.findById(commentId).orElseThrow();

        // 임시방편
        if (comment.getDeletedAt()!=null) {
            throw new RuntimeException();
        }
        comment.setDeletedAt(OffsetDateTime.now());

        Comment saved = commentRepositoryAdapter.save(comment);

        return DeleteCommentResult.from(saved);
    }
}
