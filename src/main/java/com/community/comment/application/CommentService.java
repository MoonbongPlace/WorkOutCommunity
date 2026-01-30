package com.community.comment.application;

import com.community.board.domain.model.Post;
import com.community.board.infra.persistence.PostRepositoryAdapter;
import com.community.comment.api.dto.CreateCommentRequest;
import com.community.comment.domain.model.Comment;
import com.community.comment.infra.persistance.CommentRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepositoryAdapter postRepositoryAdapter;
    private final CommentRepositoryAdapter commentRepositoryAdapter;

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
}
