package com.community.comment.domain.model;

import com.community.comment.api.dto.request.CreateCommentRequest;
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
    private Long memberId;

    @Column(nullable = false)
    private Long postId;

    @Column
    private String content;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime updatedAt;

    @Column
    private OffsetDateTime deletedAt;

    // 댓글 생성
    public static Comment create(CreateCommentRequest request, Long memberId, Long postId) {
        Comment comment = new Comment();
        comment.setMemberId(memberId);
        comment.setPostId(postId);
        comment.setContent(request.getContent());
        comment.setCreatedAt(OffsetDateTime.now());

        return comment;
    }
}
