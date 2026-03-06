package com.community.board.application;

import com.community.board.api.dto.request.CreatePostRequest;
import com.community.board.application.dto.*;
import com.community.board.api.dto.request.UpdatePostRequest;
import com.community.board.domain.model.Post;
import com.community.board.infra.persistence.PostRepositoryAdapter;
import com.community.global.component.ImageStorage;
import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.member.domain.model.Member;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private static final int MAX_IMAGES = 6;

    private final PostRepositoryAdapter postRepositoryAdapter;
    private final MemberRepositoryAdapter memberRepositoryAdapter;
    private final ImageStorage profileImageStorage;

    // 게시글 리스트 조회
    @Transactional(readOnly = true)
    public PostListResult getPostList(Long categoryId, Pageable pageable) {
        Page<Post> page = categoryId != null
                ? postRepositoryAdapter.findAllActiveByVisibilityAndCategoryId(categoryId, pageable)
                : postRepositoryAdapter.findAllActiveByVisibility(pageable);

        List<PostListItem> items = page.getContent().stream()
                .map(post -> {
                    Member member = memberRepositoryAdapter.findById(post.getMemberId())
                            .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));
                    return PostListItem.from(post, member);
                })
                .toList();

        return PostListResult.from(items, page);
    }

    // 특정 게시글 상세 조회
    @Transactional(readOnly = true)
    public DetailPostResult getPostDetail(final Long postId) {
        Post post = postRepositoryAdapter.findActiveVisibleById(postId)
                .orElseThrow(()-> new CommonException(ResponseCode.POST_NOT_FOUND));

        Member member = memberRepositoryAdapter.findById(post.getMemberId())
                .orElseThrow(()-> new CommonException(ResponseCode.MEMBER_NOT_FOUND));
        return DetailPostResult.from(post, member);
    }

    // 게시글 생성 (이미지 최대 6장)
    @Transactional
    public CreatePostResult create(Long memberId, final CreatePostRequest request, List<MultipartFile> images) {
        List<String> imageUrls = uploadImages(Collections.emptyList(), images);

        Post post = Post.create(memberId, request, imageUrls);

        Post saved = postRepositoryAdapter.save(post);

        return CreatePostResult.from(saved);
    }

    // 게시글 수정 (keepImages: 유지할 기존 이미지 URL, images: 새로 업로드할 이미지)
    @Transactional
    public UpdatePostResult update(final UpdatePostRequest request, Long postId, List<MultipartFile> images) {
        Post post = postRepositoryAdapter.findActiveById(postId)
                .orElseThrow(()-> new CommonException(ResponseCode.POST_NOT_FOUND));

        List<String> keepImages = request.getKeepImages() != null ? request.getKeepImages() : Collections.emptyList();
        List<String> updatedImageUrls = uploadImages(keepImages, images);

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategoryId(request.getCategoryId());
        post.setImages(updatedImageUrls);
        post.setUpdatedAt(OffsetDateTime.now());

        Post saved = postRepositoryAdapter.save(post);

        return UpdatePostResult.from(saved);
    }

    // 내 게시글 리스트 조회
    @Transactional(readOnly = true)
    public PostListResult getMyPostList(Long memberId, Pageable pageable) {
        Page<Post> page = postRepositoryAdapter.findAllByMemberId(memberId, pageable);

        List<PostListItem> items = page.getContent().stream()
                .map(post -> {
                    Member member = memberRepositoryAdapter.findById(post.getMemberId())
                            .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));
                    return PostListItem.from(post, member);
                })
                .toList();

        return PostListResult.from(items, page);
    }

    // 게시글 삭제
    @Transactional
    public DeletePostResult delete(Long postId) {
        Post post = postRepositoryAdapter.findActiveById(postId).orElseThrow();

        // 임시 방편 체크
        if (post.getDeletedAt() != null){
            throw new RuntimeException();
        }

        post.setDeletedAt(OffsetDateTime.now());

        Post saved = postRepositoryAdapter.save(post);

        return DeletePostResult.from(saved);
    }

    // 이미지 업로드 헬퍼: keepImages + 신규 업로드 = 최종 이미지 목록 (최대 6장)
    private List<String> uploadImages(List<String> keepImages, List<MultipartFile> newFiles) {
        List<String> result = new ArrayList<>(keepImages);

        if (newFiles != null) {
            for (MultipartFile file : newFiles) {
                if (file == null || file.isEmpty()) continue;
                if (result.size() >= MAX_IMAGES) {
                    throw new CommonException(ResponseCode.POST_IMAGE_LIMIT_EXCEEDED);
                }
                result.add(profileImageStorage.storePost(file));
            }
        }

        return result;
    }
}
