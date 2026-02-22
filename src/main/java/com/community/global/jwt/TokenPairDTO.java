package com.community.global.jwt;

public record TokenPairDTO (
        String accessToken,
        String refreshToken,
        long atExp,
        long rfExp
){}
