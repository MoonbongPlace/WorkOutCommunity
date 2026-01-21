package com.community.board.application;

import com.community.board.api.dto.CreatePostRequest;
import com.community.board.api.dto.PostDetailDTO;
import com.community.board.domain.model.Post;
import com.community.board.domain.repository.PostRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

    private PostRepository postRepository;

    // 특정 게시글 상세 조회
    @Transactional(readOnly = true)
    public PostDetailDTO getPostDetail(final Long postId) {
        final Post post = postRepository.findById(postId)
                .orElseThrow();

        return PostDetailDTO.from(post);
    }

    // 게시글 생성
    public Post create(final CreatePostRequest request) {
        Post post = Post.fromRequest(request);

        return postRepository.save(post);
    }



}
