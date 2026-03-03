package com.community.workout.api;

import com.community.global.CustomUserPrincipal;
import com.community.workout.api.dto.request.CreateWorkOutLogRequest;
import com.community.workout.api.dto.request.ReplaceExerciseRequest;
import com.community.workout.api.dto.response.*;
import com.community.workout.api.dto.request.UpdateWorkOutLogRequest;
import com.community.workout.application.*;
import com.community.workout.application.dto.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WorkOut Log", description = "운동 일지 API")
@SecurityRequirement(name = "bearerAuth")
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

    // 운동 상세 일지 조회
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
    @GetMapping("/list")
    public ResponseEntity<WorkOutLogListResponse> getWorkOutList(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ){
        Long memberId = principal.memberId();

        WorkOutLogListResult workOutLogListResult = workOutLogService.getListWorkOutLog(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WorkOutLogListResponse.from(workOutLogListResult, "운동 일지 리스트 조회 성공"));
    }

    // 운동 일지 수정 : 운동 제외
    @PatchMapping("/{workOutLogId}")
    public ResponseEntity<UpdateWorkOutLogResponse> updateWorkOutLog(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable("workOutLogId") Long workOutLogId,
            @RequestBody @Valid UpdateWorkOutLogRequest request
    ){
        Long memberId = principal.memberId();

        UpdateWorkOutLogResult updateWorkOutLogResult = workOutLogService.updateWorkOutLog(request, memberId, workOutLogId);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(UpdateWorkOutLogResponse.from(updateWorkOutLogResult, "운동 일지 수정 성공"));
    }

    // 운동 일지 수정 : 운동 교체
    @PostMapping("/{logId}/items/{itemId}/replace-exercise")
    public ResponseEntity<ReplaceExerciseResponse> replaceExercise(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable("logId") Long logId,
            @PathVariable("itemId") Long itemId,
            @RequestBody @Valid ReplaceExerciseRequest request
    ){
        Long memberId = principal.memberId();

        ReplaceExerciseResult replaceExerciseResult = workOutLogService.replaceExercise(memberId, logId, itemId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ReplaceExerciseResponse.from(replaceExerciseResult, "운동 교체 성공"));
    }

    // 운동 일지 삭제
    @DeleteMapping("/{workOutLogId}")
    public ResponseEntity<DeleteWorkOutLogResponse> deleteWorkOutLog(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable("workOutLogId") Long workOutLogId
    ){
        Long memberId = principal.memberId();

        DeleteWorkOutLogResult deleteWorkOutLogResult = workOutLogService.deleteWorkOutLog(memberId, workOutLogId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(DeleteWorkOutLogResponse.from(deleteWorkOutLogResult, "운동 일지 삭제 성공"));
    }

    // 운동 일지 삭제 : 운동 일지 내 WorkOutLogItem 삭제
    @DeleteMapping("/{workOutLogId}/items/{itemId}")
    public ResponseEntity<DeleteWorkOutLogItemResponse> deleteWorkOutLogItem(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable("workOutLogId") Long workOutLogId,
            @PathVariable("itemId") Long itemId
    ) {
        Long memberId = principal.memberId();

        DeleteWorkOutLogItemResult deleteWorkOUtLogItemResult = workOutLogService
                .deleteWorkOutLogItem(memberId, workOutLogId, itemId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(DeleteWorkOutLogItemResponse.from(deleteWorkOUtLogItemResult,"운동 항목 삭제 완료."));
    }

}
