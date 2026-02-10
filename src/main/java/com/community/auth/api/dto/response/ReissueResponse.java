package com.community.auth.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReissueResponse {
    String grantType;
    String accessToken;
    String refreshToken;
    long atExpIn;
    long rtExpIn;

    public static ReissueResponse from(String bearer, String newAccessToken, String newRefreshToken, long atExpSeconds, long rtExpSeconds) {
        return new ReissueResponse(
                bearer,
                newAccessToken,
                newRefreshToken,
                atExpSeconds,
                rtExpSeconds
        );
    }
}
