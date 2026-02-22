package com.community.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    TOKEN_MISSING("4019", HttpStatus.UNAUTHORIZED, "토큰이 없습니다."),
    TOKEN_EXPIRED("4018", HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_INVALID("4017", HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN("4016", HttpStatus.UNAUTHORIZED, "Refresh 토큰이 유효하지 않습니다."),
    INVALID_PASSWORD("4015", HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."), // 유지 or 400으로 변경

    MEMBER_NOT_FOUND("4041", HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    POST_NOT_FOUND("4042", HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND("4043", HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    NOTIFICATION_NOT_FOUND("4044", HttpStatus.NOT_FOUND, "알림을 찾을 수 없습니다."),

    NOTIFICATION_FORBIDDEN("4031", HttpStatus.FORBIDDEN, "해당 알림에 접근할 수 없습니다."),
    MEMBER_ALREADY_DELETED("4032",HttpStatus.FORBIDDEN ,"DELETED 회원 입니다." ),
    POST_ALREADY_DELETED("4033",HttpStatus.FORBIDDEN ,"DELETED 게시글 입니다." ),
    INVALID_MEMBER("4033",HttpStatus.FORBIDDEN ,"작성자과 ID가 일치하지 않습니다. " );

    private final String code;
    private final HttpStatus status;
    private final String message;
}
