//package com.community.auth.api.dto.response;
//
//import com.community.auth.application.dto.PhoneVerifyResultResult;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import org.springframework.http.HttpStatus;
//
//import java.time.OffsetDateTime;
//
//@Getter
//@AllArgsConstructor
//public class VerifyResultResponse {
//    private String code;
//    private String message;
//    private OffsetDateTime timestamp;
//    private PhoneVerifyResultResult phoneVerifyResultResult;
//
//    public static VerifyResultResponse from(PhoneVerifyResultResult phoneVerifyResultResult, String message) {
//        return new VerifyResultResponse(
//                String.valueOf(HttpStatus.OK.value()),
//                message,
//                OffsetDateTime.now(),
//                phoneVerifyResultResult
//        );
//    }
//}
