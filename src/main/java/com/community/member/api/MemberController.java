package com.community.member.api;

import com.community.member.api.dto.request.UpdateMemberRequest;
import com.community.member.api.dto.response.MemberInfoResponse;
import com.community.member.api.dto.response.UpdateMemberResponse;
import com.community.member.application.MemberService;
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

//    @PostMapping("/me")
//    public ResponseEntity<UpdateMemberResponse> updateMember(
//            @RequestBody @Valid final UpdateMemberRequest request
//    ){
//        UpdatedMemberResult updatedMember = memberService.update(request);
//    }
}
