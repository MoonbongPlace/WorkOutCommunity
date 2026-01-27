package com.community.board.application;

import com.community.board.api.dto.request.CreatePostRequest;
import com.community.board.application.dto.CreatePostResult;
import com.community.board.application.dto.DeletePostResult;
import com.community.board.application.dto.DetailPostResult;
import com.community.board.api.dto.request.UpdatePostRequest;
import com.community.board.application.dto.UpdatePostResult;
import com.community.board.domain.model.Post;
import com.community.board.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.time.OffsetDateTime.now;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 특정 게시글 상세 조회
    @Transactional(readOnly = true)
    public DetailPostResult getPostDetail(final Long postId) {
        final Post post = postRepository.findById(postId)
                .orElseThrow();

        return DetailPostResult.from(post);
    }

    // 게시글 생성
    @Transactional
    public CreatePostResult create(final CreatePostRequest request) {
        Post post = Post.fromRequest(request);

        Post saved = postRepository.save(post);

        return CreatePostResult.from(saved);
    }

    // 게시글 수정
    @Transactional
    public UpdatePostResult update(final UpdatePostRequest request, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategoryId(request.getCategoryId());
        post.setImage(request.getImage());

        Post saved = postRepository.save(post);

        return UpdatePostResult.from(saved);
    }

    @Transactional
    public DeletePostResult delete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();

        // 임시 방편 체크
        if (post.getDeletedAt() != null){
            throw new RuntimeException();
        }

        post.setDeletedAt(now());
        Post saved = postRepository.save(post);

        return DeletePostResult.from(saved);
    }
}
