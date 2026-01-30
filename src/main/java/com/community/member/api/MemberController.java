package com.community.member.api;

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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    // Principal 추가 예정
    // 마이페이지 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> showMypage(){

        // 임시
        DetailMemberResult detailMember = memberService.getMemberProfile(0L);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(MemberInfoResponse.get(detailMember, "내 정보 조회 성공"));
    }

    // 마이페이지 내 정보 수정
    @PutMapping("/me")
    public ResponseEntity<UpdateMemberResponse> updateMember(
            @RequestBody @Valid final UpdateMemberRequest request
    ){

        // id 인자 값 삭제 예정
        UpdatedMemberResult updatedMember = memberService.updateProfile(request, 0L);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UpdateMemberResponse.from(updatedMember, "내 정보 수정 성공"));
    }

    // 계정 탈퇴
    @DeleteMapping("/{memberId}")
    public ResponseEntity<DeleteMemberResponse> deleteMember(
            @PathVariable(name="memberId") final Long memberId
    ){
        DeletedMemberResult deletedMember = memberService.withDrawProfile(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(DeleteMemberResponse.from(deletedMember, "계정 탈퇴 성공"));
    }
}
