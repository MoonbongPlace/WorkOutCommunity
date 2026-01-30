package com.community.member.api.dto.response;

import com.community.member.application.dto.UpdatedMemberResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private UpdatedMemberResult updatedMemberResult;

    public static UpdateMemberResponse from(UpdatedMemberResult updatedMember, String message) {
        return new UpdateMemberResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                updatedMember
        );
    }
}
