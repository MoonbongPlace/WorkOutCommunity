package com.community.board.application.dto;

import com.community.board.domain.model.Post;
import com.community.member.domain.model.Member;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "memberId", "memberName", "title", "content", "categoryId", "views", "images", "createdAt", "likeCount","profileImage"})
public class DetailPostResult {
    private Long id;
    private Long member_id;
    private String memberName;
    private String title;
    private String content;
    private Long categoryId;
    private int views;
    private List<String> images;
    private OffsetDateTime createdAt;
    private int likeCount;
    private String profileImage;

    public static DetailPostResult from(Post post, Member member){
        return new DetailPostResult(
                post.getId(),
                post.getMemberId(),
                member.getMemberName(),
                post.getTitle(),
                post.getContent(),
                post.getCategoryId(),
                post.getViews(),
                post.getImages(),
                post.getCreatedAt(),
                post.getLikeCount(),
                member.getProfileImage()
        );
    }
}
