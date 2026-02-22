package com.community.global.exception;

public class CommonException extends RuntimeException {
    private final ResponseCode code;

    public ResponseCode code() {
        return code;
    }

    public CommonException(ResponseCode code) {
        super(code != null ? code.getMessage() : null);
        this.code = code;
    }
}
