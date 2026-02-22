package com.community.member.api;

import com.community.global.CustomUserPrincipal;
import com.community.member.api.dto.request.UpdateMemberRequest;
import com.community.member.api.dto.response.DeleteMemberResponse;
import com.community.member.api.dto.response.MemberInfoResponse;
import com.community.member.api.dto.response.UpdateMemberResponse;
import com.community.member.application.MemberService;
import com.community.member.application.dto.DeletedMemberResult;
import com.community.member.application.dto.DetailMemberResult;
import com.community.member.application.dto.UpdatedMemberResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

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
    @PutMapping("/me")
    public ResponseEntity<UpdateMemberResponse> updateMember(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody @Valid final UpdateMemberRequest request
    ){
        Long memberId = principal.memberId();
        UpdatedMemberResult updatedMember = memberService.updateProfile(request, memberId);

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
