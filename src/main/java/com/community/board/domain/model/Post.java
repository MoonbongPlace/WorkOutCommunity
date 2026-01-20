package com.community.board.domain.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "posts")
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

    protected Post() {}

    public Post(String title, String body, String category, Long authorId, String image) {
        this.title = title;
        this.body = body;
        this.category = category;
        this.authorId = authorId;
        this.image = image;
        this.createdAt = OffsetDateTime.now();
    }

    public void increaseViews() {
        this.views++;
    }
}
