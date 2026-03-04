package com.community.member.api;

import com.community.global.CustomUserPrincipal;
import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.member.api.dto.request.UpdateMemberRequest;
import com.community.member.api.dto.response.DeleteMemberResponse;
import com.community.member.api.dto.response.MemberInfoResponse;
import com.community.member.api.dto.response.UpdateMemberResponse;
import com.community.member.application.MemberService;
import com.community.member.application.dto.DeletedMemberResult;
import com.community.member.application.dto.DetailMemberResult;
import com.community.member.application.dto.UpdatedMemberResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Member", description = "회원 정보 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    // 마이페이지 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> showMypage(
            @AuthenticationPrincipal CustomUserPrincipal principal
            ){

        Long memberId = principal.memberId();
        DetailMemberResult detailMember = memberService.getMemberProfile(memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(MemberInfoResponse.get(detailMember, "내 정보 조회 성공"));
    }

    // 마이페이지 내 정보 수정
    @PatchMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UpdateMemberResponse> updateMember(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestPart("data") String data,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) throws Exception {
        Long memberId = principal.memberId();

        UpdateMemberRequest request = objectMapper.readValue(data, UpdateMemberRequest.class);

        boolean noFields =
                (request.getMemberName() == null || request.getMemberName().isBlank()) &&
                        (request.getName() == null || request.getName().isBlank()) &&
                        (request.getAge() == null) &&
                        (request.getSex() == null || request.getSex().isBlank()) &&
                        (profileImage == null || profileImage.isEmpty());

        if (noFields) {
            throw new CommonException(ResponseCode.NO_FIELDS_TO_UPDATE);
        }

        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new CommonException(ResponseCode.INVALID_REQUEST);
        }

        UpdatedMemberResult updatedMember = memberService.updateProfile(request, memberId, profileImage);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UpdateMemberResponse.from(updatedMember, "내 정보 수정 성공"));
    }

    // 계정 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<DeleteMemberResponse> deleteMember(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ){
        Long memberId = principal.memberId();
        DeletedMemberResult deletedMember = memberService.withDrawProfile(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(DeleteMemberResponse.from(deletedMember, "계정 탈퇴 성공"));
    }
}
