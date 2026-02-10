package com.community.global;

public record TokenPairDTO (
        String accessToken,
        String refreshToken,
        long atExp,
        long rfExp
){}
