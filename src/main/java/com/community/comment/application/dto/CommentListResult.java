package com.community.comment.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentListResult {
    private List<CommentDetailResult> comments;
}
