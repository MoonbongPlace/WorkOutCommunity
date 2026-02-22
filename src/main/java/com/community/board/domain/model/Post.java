package com.community.board.domain.model;

import com.community.board.api.dto.request.CreatePostRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

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

    private String image;

    @Column(name="created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name="updated_at")
    private OffsetDateTime updatedAt;

    @Column(name="deleted_at", updatable = false)
    private OffsetDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name="visibility", nullable = false)
    private PostVisibility postVisibility;

    public static Post fromRequest(Long memberId, CreatePostRequest request) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        // 삭제 예정
        post.setMemberId(memberId);
        post.setContent(request.getContent());
        post.setCategoryId(request.getCategoryId());
        post.setImage(request.getImage());
        post.setPostVisibility(PostVisibility.VISIBLE);
        post.setCreatedAt(OffsetDateTime.now());
        return post;
    }

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
