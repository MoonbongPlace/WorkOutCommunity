package com.community.workout.api;


import com.community.global.CustomUserPrincipal;
import com.community.workout.api.dto.request.SaveRecommendedRoutineRequest;
import com.community.workout.api.dto.response.SaveRecommendedRoutineResponse;
import com.community.workout.api.dto.response.WorkOutLogTodayResponse;
import com.community.workout.application.WorkOutLogService;
import com.community.workout.domain.model.WorkOutLog;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workout-logs")
public class WorkOutLogController {
    private final WorkOutLogService workOutLogService;

    @PostMapping("/today/items")
    public ResponseEntity<SaveRecommendedRoutineResponse> saveTodayItems(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestBody @Valid SaveRecommendedRoutineRequest request
    ) {
        Long memberId = principal.memberId();
        LocalDate today = LocalDate.now();

        Long logId = workOutLogService.saveRecommendedRoutine(memberId, today, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SaveRecommendedRoutineResponse.of(logId));
    }

    @GetMapping("/today")
    public ResponseEntity<WorkOutLogTodayResponse> getToday(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Long memberId = principal.memberId();
        LocalDate today = LocalDate.now();

        WorkOutLog log = workOutLogService.getTodayLog(memberId, today);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WorkOutLogTodayResponse.from(log));
    }
}
