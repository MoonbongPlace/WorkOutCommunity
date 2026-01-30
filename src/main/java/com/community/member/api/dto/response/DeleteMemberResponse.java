package com.community.member.api.dto.response;

import com.community.member.application.dto.DeletedMemberResult;
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
public class DeleteMemberResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private DeletedMemberResult deletedMemberResult;

    public static DeleteMemberResponse from(DeletedMemberResult deletedMember, String message) {
        return new DeleteMemberResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                deletedMember
        );
    }
}
