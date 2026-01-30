package com.community.comment.infra.persistance;

import com.community.comment.domain.model.Comment;
import com.community.comment.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentRepositoryAdapter implements CommentRepository {
    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Comment save(Comment comment) {
        return commentJpaRepository.save(comment);
    }
}
