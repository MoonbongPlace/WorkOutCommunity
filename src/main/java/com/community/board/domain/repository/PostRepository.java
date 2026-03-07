package com.community.board.domain.repository;

import com.community.board.domain.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findActiveById(Long id);
    List<Post> findLatest(int size);
    void deleteById(Long id);

    // 게시글 작성자 조회
    Optional<Long> findAuthorIdByPostId(Long postId);

    // 특정 게시글 조회 : 숨김 처리 활성화
    Optional<Post> findActiveVisibleById(Long postId);

    // 게시글 리스트 조회 : 숨김 처리 활성화
    Page<Post> findAllActiveByVisibility(Pageable pageable);

    // 게시글 전체 조회 : 숨김 처리 비활성화
    Page<Post> findAll(Pageable pageable);

    // 내 게시글 조회
    Page<Post> findAllByMemberId(Long memberId, Pageable pageable);

    // 카테고리별 게시글 조회
    Page<Post> findAllActiveByVisibilityAndCategoryId(Long categoryId, Pageable pageable);

    Optional<String> findPostTitleById(Long postId);

    boolean existsById(Long postId);
}
