package com.community.board.api.dto;

import com.community.board.domain.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private PostDetailDTO post;

    public static PostResponse get(PostDetailDTO postDetailDTO, String message){
        return new PostResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                postDetailDTO
        );
    }

//    public static PostResponse create(Post createdPost, String message) {
//    }
}