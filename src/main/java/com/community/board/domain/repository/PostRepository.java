package com.community.board.domain.repository;

import com.community.board.domain.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(Long id);
    List<Post> findLatest(int size);
    void deleteById(Long id);

    Optional<Long> findAuthorIdByPostId(Long postId);
}
