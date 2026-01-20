package com.community.board.infra.persistence;

import com.community.board.domain.model.Post;
import com.community.board.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
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
        public Optional<Post> findById(Long id) {
                return postJpaRepository.findById(id);
        }

        @Override
        public List<Post> findLatest(int size) {
                return postJpaRepository.findAllByOrderByCreatedAtDesc((Pageable) PageRequest.of(0, size));
        }

        @Override
        public void deleteById(Long id) {
                postJpaRepository.deleteById(id);
        }
}
