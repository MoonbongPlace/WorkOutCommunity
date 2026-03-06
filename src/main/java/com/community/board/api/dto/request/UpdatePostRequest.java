package com.community.board.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequest {
    @NotNull
    @Size(max = 255)
    String title;
    String content;
    Long categoryId;
    // 수정 후에도 유지할 기존 이미지 URL 목록 (null 또는 빈 목록이면 기존 이미지 전체 제거)
    List<String> keepImages = Collections.emptyList();
}