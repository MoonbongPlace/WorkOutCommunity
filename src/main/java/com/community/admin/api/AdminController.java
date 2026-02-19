package com.community.admin.api;

import com.community.admin.application.AdminMemberListResult;
import com.community.admin.application.AdminMemberStatusUpdateResult;
import com.community.admin.application.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    // 관리자 : 회원 조회
    @GetMapping
    public ResponseEntity<AdminMemberListResponse> memberList(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ){
        AdminMemberListResult adminMemberListResult = adminService.list(pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(AdminMemberListResponse.from(adminMemberListResult, "회원 조회 성공"));
    }

    // 관리자 : 회원 상태 변경
    @PatchMapping("/{memberId}/status")
    public ResponseEntity<AdminMemberStatusUpdateResponse> updateStatus(
            @PathVariable Long memberId,
            @RequestBody @Valid AdminMemberStatusUpdateRequest request
    ) {
        AdminMemberStatusUpdateResult result = adminService.updateStatus(memberId, request);
        return ResponseEntity.ok(AdminMemberStatusUpdateResponse.from(result, "회원 상태 변경 성공"));
    }
}
