package com.community.comment.application.dto;

public record PostCommentCountRow(
        Long postId,
        Long commentCount
) {}