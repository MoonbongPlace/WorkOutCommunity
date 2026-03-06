package com.community.board.infra.persistence;

import com.community.board.domain.model.Post;
import com.community.board.domain.model.PostVisibility;
import com.community.board.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryAdapter implements PostRepository {

        private final PostJpaRepository postJpaRepository;

        @Override
        public Post save(Post post){
                return postJpaRepository.save(post);
        }

        @Override
        public Optional<Post> findActiveById(Long id) {
                return postJpaRepository.findById(id);
        }

        @Override
        public List<Post> findLatest(int size) {
                return postJpaRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, size));
        }

        @Override
        public void deleteById(Long id) {
                postJpaRepository.deleteById(id);
        }

        @Override
        public Optional<Long> findAuthorIdByPostId(Long postId) {
                return postJpaRepository.findMemberIdByPostId(postId);
        }


        @Override
        public Optional<Post> findActiveVisibleById(Long postId) {
                return postJpaRepository.findActiveVisibleById(postId);
        }

        @Override
        public Page<Post> findAllActiveByVisibility( Pageable pageable) {
                return postJpaRepository.findAllActiveByVisibility(PostVisibility.VISIBLE,pageable);
        }

        @Override
        public Page<Post> findAll(Pageable pageable) {
                return postJpaRepository.findAll(pageable);
        }

        @Override
        public Page<Post> findAllByMemberId(Long memberId, Pageable pageable) {
                return postJpaRepository.findAllByMemberId(memberId, pageable);
        }

        @Override
        public Page<Post> findAllActiveByVisibilityAndCategoryId(Long categoryId, Pageable pageable) {
                return postJpaRepository.findAllByDeletedAtIsNullAndPostVisibilityAndCategoryId(
                        PostVisibility.VISIBLE, categoryId, pageable);
        }
}
