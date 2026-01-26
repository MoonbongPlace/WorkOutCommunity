package com.community.board.application;

import com.community.board.api.dto.CreatePostRequest;
import com.community.board.api.dto.PostCreateResult;
import com.community.board.api.dto.PostDetailDTO;
import com.community.board.api.dto.UpdatePostRequest;
import com.community.board.application.dto.PostUpdateResult;
import com.community.board.domain.model.Post;
import com.community.board.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 특정 게시글 상세 조회
    @Transactional(readOnly = true)
    public PostDetailDTO getPostDetail(final Long postId) {
        final Post post = postRepository.findById(postId)
                .orElseThrow();

        return PostDetailDTO.from(post);
    }

    // 게시글 생성
    @Transactional
    public PostCreateResult create(final CreatePostRequest request) {
        Post post = Post.fromRequest(request);

        Post saved = postRepository.save(post);

        return PostCreateResult.from(saved);
    }

//    // 게시글 수정
//    @Transactional
//    public PostUpdateResult update(final UpdatePostRequest request) {
//        Post post = postRepository.findById(request)
//    }
}
