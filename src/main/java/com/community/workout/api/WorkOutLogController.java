package com.community.workout.api;

import com.community.global.CustomUserPrincipal;
import com.community.workout.api.dto.request.CreateWorkOutLogRequest;
import com.community.workout.api.dto.response.CreateWorkOutLogResponse;
import com.community.workout.api.dto.response.WorkOutLogDetailResponse;
import com.community.workout.application.CreateWorkOutLogResult;
import com.community.workout.application.WorkOutLogDetailResult;
import com.community.workout.application.WorkOutLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workout-logs")
public class WorkOutLogController {

    private final WorkOutLogService workOutLogService;

    // 운동 일지 등록
    @PostMapping
    public ResponseEntity<CreateWorkOutLogResponse> createWorkOutLog(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody @Valid CreateWorkOutLogRequest request
    ) {
        Long memberId = principal.memberId();

        CreateWorkOutLogResult createWorkOutLogResult = workOutLogService.createWorkOutLog(memberId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CreateWorkOutLogResponse.from(createWorkOutLogResult, "운동 일지 생성 완료."));
    }

    // 운동 일지 조회
    @GetMapping("/{workOutLogId}")
    public ResponseEntity<WorkOutLogDetailResponse> getToday(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable("workOutLogId") Long workOutLogId

    ) {
        Long memberId = principal.memberId();

        WorkOutLogDetailResult workOutLogDetailResult = workOutLogService.getDetailWorkOutLog(memberId, workOutLogId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WorkOutLogDetailResponse.from(workOutLogDetailResult, "운동 일지 조회 성공"));
    }

    // 운동 일지 리스트 조회
}
