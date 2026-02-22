package com.community.notification.api;

import com.community.global.CommonException;
import com.community.global.CustomUserPrincipal;
import com.community.global.ResponseCode;
import com.community.notification.api.dto.request.NotificationCreateRequest;
import com.community.notification.api.dto.response.*;
import com.community.notification.application.*;
import com.community.notification.application.dto.MyNotificationsResult;
import com.community.notification.application.dto.NotificationCreateResult;
import com.community.notification.application.dto.ReadOneResult;
import com.community.notification.application.dto.UnReadCountResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // 내 알림 조회
    @GetMapping
    public ResponseEntity<NotificationResponse> myNotifications(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            Pageable pageable
    ) {
        Long memberId = principal.memberId();
        MyNotificationsResult myNotificationsResult = notificationService.getMyNotifications(memberId, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        NotificationResponse.from(myNotificationsResult, "내 알림 조회 성공")
                );
    }

    // 안 읽음 알림 개수
    @GetMapping("/unread-count")
    public ResponseEntity<NotificationUnreadCountResponse> unreadCount(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Long memberId = principal.memberId();

        UnReadCountResult unReadCountResult = notificationService.getUnreadCount(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(NotificationUnreadCountResponse.from(unReadCountResult, "안 읽은 알림 개수 조회 성공"));
    }

    // 알림 단건 읽음
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationReadOneResponse> readOne(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable("notificationId") Long notificationId
    ) {
        Long memberId = principal.memberId();

        ReadOneResult readOneResult = notificationService.readNotification(memberId, notificationId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(NotificationReadOneResponse.from(readOneResult, "알림 읽음 처리 성공"));
    }


    // 알림 전체 읽음
    @PatchMapping("/read-all")
    public ResponseEntity<NotificationReadAllResponse> readAll(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Long memberId = principal.memberId();

        int updated = notificationService.readAllNotification(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(NotificationReadAllResponse.of("전체 알림 읽음 처리 성공", updated));
    }


    // 알림 생성
    @PostMapping
    public ResponseEntity<NotificationCreateResponse> create(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody @Valid NotificationCreateRequest request
    ) {
        Long senderId = principal.memberId();
        if (senderId == null) {
            throw new CommonException(ResponseCode.MEMBER_NOT_FOUND);
        }

        NotificationCreateResult notificationCreateResult = notificationService.createNotificationFromApi(senderId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(NotificationCreateResponse.from(notificationCreateResult, "알림 생성 성공"));
    }

}