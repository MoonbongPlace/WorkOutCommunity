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

    @Lob
    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Long views = 0L;

    @Column(nullable = false)
    private Long authorId; // User ID 참조 (객체 참조 X)

    private String image;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;


    public Post(String title, String body, String category, Long authorId, String image) {
        this.title = title;
        this.body = body;
        this.category = category;
        this.authorId = authorId;
        this.image = image;
        this.createdAt = OffsetDateTime.now();
    }

    public static Post fromRequest(CreatePostRequest request) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setBody(request.getBody());
        post.setCategory(request.getCategory());
        post.setImage(request.getImage());
        return post;
    }

    public void increaseViews() {
        this.views++;
    }
}
