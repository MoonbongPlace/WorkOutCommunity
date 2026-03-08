package com.community.admin.api;

import com.community.admin.api.dto.request.AdminMemberStatusUpdateRequest;
import com.community.admin.api.dto.request.AdminNotificationBroadcastRequest;
import com.community.admin.api.dto.request.AdminPostVisibilityUpdateRequest;
import com.community.admin.api.dto.response.*;
import com.community.admin.application.AdminMemberDetailResult;
import com.community.admin.application.dto.AdminMemberListResult;
import com.community.admin.application.dto.AdminMemberStatusUpdateResult;
import com.community.admin.application.dto.AdminPostDetailResult;
import com.community.admin.application.dto.AdminPostListResult;
import com.community.admin.application.dto.AdminPostVisibilityUpdateResult;
import com.community.admin.application.AdminService;
import com.community.member.domain.model.MemberStatus;
import com.community.global.CustomUserPrincipal;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin", description = "관리자 전용 API")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    // 관리자 : 회원 리스트 조회
    @GetMapping("/members")
    public ResponseEntity<AdminMemberListResponse> memberList(
            @RequestParam(required = false) MemberStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        AdminMemberListResult adminMemberListResult = adminService.memberList(status, pageable);

        return ResponseEntity.ok(AdminMemberListResponse.from(adminMemberListResult, "관리자 : 회원 조회 성공"));
    }

    // 관리자 : 회원 상세 조회
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/members/{memberId}")
    public ResponseEntity<AdminMemberDetailResponse> memberDetail(
            @PathVariable("memberId") Long memberId
    ){
        AdminMemberDetailResult adminMemberDetailResult = adminService.getDetailMember(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(AdminMemberDetailResponse.from(adminMemberDetailResult, "관리자 : 회원 상세 정보 조회 성공"));
    }

    // 관리자 : 회원 상태 변경
    @PatchMapping("/{memberId}/status")
    public ResponseEntity<AdminMemberStatusUpdateResponse> updateStatus(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long memberId,
            @RequestBody @Valid AdminMemberStatusUpdateRequest request
    ) {
        Long adminId = principal.memberId();

        AdminMemberStatusUpdateResult result = adminService.updateMemberStatus(adminId, memberId, request);

        return ResponseEntity.ok(AdminMemberStatusUpdateResponse.from(result, "관리자 : 회원 상태 변경 성공"));
    }

    // 관리자 : 게시글 조회
    @GetMapping("/posts")
    public ResponseEntity<AdminPostListResponse> postList(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        AdminPostListResult adminPostListResult = adminService.postList(pageable);

        return ResponseEntity.ok(AdminPostListResponse.from(adminPostListResult, "관리자 : 게시글 조회 성공"));
    }

    // 관리자 : 게시글 상세 조회 : 숨김 처리된 게시글 포함
    @GetMapping("/posts/{postId}")
    public ResponseEntity<AdminPostDetailResponse> detailPost(
            @PathVariable(name = "postId") Long postId
    ) {
        AdminPostDetailResult adminPostDetailResult = adminService.detailPost(postId);

        return ResponseEntity.ok(AdminPostDetailResponse.from(adminPostDetailResult, "관리자 : 게시글 상세 조회 성공"));
    }

    // 관리자 : 게시글 상태 변경
    @PatchMapping("/posts/{postId}/visibility")
    public ResponseEntity<AdminPostVisibilityUpdateResponse> updatePostVisibility(
            @RequestBody @Valid AdminPostVisibilityUpdateRequest request,
            @PathVariable Long postId
    ) {
        AdminPostVisibilityUpdateResult adminPostVisibilityUpdateResult = adminService.updatePostVisibility(request.getPostVisibility(), postId);

        return ResponseEntity.ok(
                AdminPostVisibilityUpdateResponse.from(adminPostVisibilityUpdateResult, "관리자 : 게시글 상태 변경 성공"));
    }

    // 관리자 : 알림 브로드 캐스트
    @PostMapping("/broadcast")
    public ResponseEntity<AdminNotificationBroadcastResponse> broadcast(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody @Valid AdminNotificationBroadcastRequest request
    ) {
        long sent = adminService.broadcast(principal.memberId(), request);

        return ResponseEntity.ok(AdminNotificationBroadcastResponse.of(sent, "관리자 : 알림 브로트 캐스트 성공"));
    }
}
