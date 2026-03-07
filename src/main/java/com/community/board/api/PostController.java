package com.community.board.api;

import com.community.board.api.dto.request.UpdatePostRequest;
import com.community.board.api.dto.response.*;
import com.community.board.api.dto.request.CreatePostRequest;
import com.community.board.application.dto.PostListResult;
import com.community.board.application.PostService;
import com.community.board.application.dto.CreatePostResult;
import com.community.board.application.dto.DeletePostResult;
import com.community.board.application.dto.DetailPostResult;
import com.community.board.application.dto.UpdatePostResult;
import com.community.board.domain.model.Post;
import com.community.board.infra.persistence.PostRepositoryAdapter;
import com.community.global.exception.CommonException;
import com.community.global.CustomUserPrincipal;
import com.community.global.exception.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Post", description = "게시글 CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final PostRepositoryAdapter postRepositoryAdapter;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    // 내 게시글 리스트 조회
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<PostListResponse> listMyPost(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Long memberId = principal.memberId();
        PostListResult postListResult = postService.getMyPostList(memberId, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PostListResponse.from(postListResult, "내 게시글 리스트 조회 성공"));
    }

    // 게시글 리스트 조회
    @GetMapping
    public ResponseEntity<PostListResponse> listPost(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        PostListResult postListResult = postService.getPostList(categoryId, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PostListResponse.from(postListResult, "게시글 리스트 조회 성공"));
    }
    // 특정 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> findPost(
            @PathVariable(name = "postId") final Long postId
    ) {
        DetailPostResult detail = postService.getDetailPost(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PostResponse.get(detail, "게시글 상세 조회 성공"));
    }

    // 게시글 작성 (이미지 최대 6장)
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<CreatePostResponse> createPost(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestParam @Valid String data,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws Exception {
        Long memberId = principal.memberId();

        CreatePostRequest request = objectMapper.readValue(data, CreatePostRequest.class);

        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new CommonException(ResponseCode.INVALID_REQUEST);
        }

        CreatePostResult createdPost = postService.create(memberId, request, images);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CreatePostResponse.create(
                        createdPost,
                        "게시글 작성 완료"
                ));
    }

    // 게시글 수정 (keepImages: 유지할 기존 이미지 URL, images: 새로 업로드할 이미지, 최대 6장)
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(value = "/{postId}", consumes = "multipart/form-data")
    public ResponseEntity<UpdatePostResponse> updatePost(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable(name = "postId") final Long postId,
            @RequestParam @Valid String data,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws Exception {
        Long memberId = principal.memberId();
        Post post = postRepositoryAdapter.findActiveById(postId)
                .orElseThrow(() -> new CommonException(ResponseCode.POST_NOT_FOUND));

        if (!post.getMemberId().equals(memberId)) {
            throw new CommonException(ResponseCode.INVALID_MEMBER);
        }

        UpdatePostRequest request = objectMapper.readValue(data, UpdatePostRequest.class);

        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new CommonException(ResponseCode.INVALID_REQUEST);
        }

        UpdatePostResult updatedPost = postService.update(request, postId, images);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UpdatePostResponse.update(
                        updatedPost,
                        "게시글 수정 완료"
                ));
    }

    // 게시글 삭제
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{postId}")
    public ResponseEntity<DeletePostResponse> deletePost(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable(name = "postId") final Long postId
    ) {
        Long memberId = principal.memberId();
        Post post = postRepositoryAdapter.findActiveById(postId)
                .orElseThrow(() -> new CommonException(ResponseCode.POST_NOT_FOUND));

        if (!post.getMemberId().equals(memberId)) {
            throw new CommonException(ResponseCode.INVALID_MEMBER);
        }

        DeletePostResult deletedPost = postService.delete(postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(DeletePostResponse.delete(
                        deletedPost,
                        "게시글 삭제 완료"
                ));
    }
}
