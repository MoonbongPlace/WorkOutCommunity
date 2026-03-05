package com.community.board.domain.model;

import com.community.board.api.dto.request.CreatePostRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@Entity(name = "Post")
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @JoinColumn(name="category_id")
    private Long categoryId;

    @Column(nullable = false)
    private int views = 0;

    @Column(nullable = false)
    @JoinColumn(name="member_id")
    private Long memberId; // User ID 참조 (객체 참조 X)

    @Convert(converter = StringListConverter.class)
    @Column(name="image", length = 2048)
    private List<String> images = Collections.emptyList();

    @Column(name="created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name="updated_at")
    private OffsetDateTime updatedAt;

    @Column(name="deleted_at", updatable = false)
    private OffsetDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name="visibility", nullable = false)
    private PostVisibility postVisibility;

    @Column(name = "like_count")
    private int likeCount;

    public static Post create(Long memberId, CreatePostRequest request, List<String> imageUrls) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setMemberId(memberId);
        post.setContent(request.getContent());
        post.setCategoryId(request.getCategoryId());
        post.setImages(imageUrls != null ? imageUrls : Collections.emptyList());
        post.setPostVisibility(PostVisibility.VISIBLE);
        post.setCreatedAt(OffsetDateTime.now());
        post.setLikeCount(0);
        return post;
    }

    public void increaseLikeCount() { this.likeCount++; }

    public void increaseViews() {
        this.views++;
    }

    public void hide() {
        this.postVisibility = PostVisibility.HIDDEN;
        this.setUpdatedAt(OffsetDateTime.now());
    }

    public void show() {
        this.postVisibility = PostVisibility.VISIBLE;
        this.setUpdatedAt(OffsetDateTime.now());
    }
}
