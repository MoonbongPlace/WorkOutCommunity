package com.community.board.domain.model;

import com.community.board.api.dto.CreatePostRequest;
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

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public static Post fromRequest(CreatePostRequest request) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategoryId(request.getCategoryId());
        post.setImage(request.getImage());
        return post;
    }

    public void increaseViews() {
        this.views++;
    }
}
