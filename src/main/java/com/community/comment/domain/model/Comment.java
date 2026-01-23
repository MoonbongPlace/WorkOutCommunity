package com.community.comment.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity(name="Comment")
@Table(name="comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long member_id;

    @Column(nullable = false)
    private Long post_id;

    @Column
    private String content;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime created_at;

    @Column
    private OffsetDateTime updated_at;

    @Column
    private OffsetDateTime deleted_at;
}
