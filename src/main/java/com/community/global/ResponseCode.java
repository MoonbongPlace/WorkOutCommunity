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
    INVALID_REFRESH_TOKEN("4016",HttpStatus.UNAUTHORIZED , "Refresh 토큰이 유효하지 않습니다." ),
    INVALID_PASSWORD("4015",HttpStatus.UNAUTHORIZED ,"비밀번호가 일치하지 않습니다." ),
    // 수정 예정
    MEMBER_NOT_FOUND("4014", HttpStatus.UNAUTHORIZED, "회원이 아닙니다." ),
    POST_NOT_FOUND("4013",HttpStatus.UNAUTHORIZED , "게시글을 찾을 수 없습니다."    );

    private final String code;
    private final HttpStatus status;
    private final String message;
}
