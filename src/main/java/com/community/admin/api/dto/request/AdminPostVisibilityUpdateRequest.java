package com.community.admin.api.dto.request;

import com.community.board.domain.model.PostVisibility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminPostVisibilityUpdateRequest {
    PostVisibility postVisibility;
}
