package com.community.auth.application.dto;

import com.community.auth.domain.PhoneVerification;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PhoneVerifyResult {
    private String phoneNumber;

    public static PhoneVerifyResult from(PhoneVerification phoneVerification) {
        return new PhoneVerifyResult(
                phoneVerification.getPhoneNumber()
        );
    }

}
