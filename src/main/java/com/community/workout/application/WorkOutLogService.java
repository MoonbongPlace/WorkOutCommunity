package com.community.workout.application;

import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.workout.api.dto.request.CreateWorkOutLogRequest;
import com.community.workout.api.dto.request.ReplaceExerciseRequest;
import com.community.workout.api.dto.request.UpdateWorkOutLogRequest;
import com.community.workout.application.dto.*;
import com.community.workout.domain.model.Exercise;
import com.community.workout.domain.model.WorkOutLog;
import com.community.workout.domain.model.WorkOutLogItem;
import com.community.workout.infra.persistence.adapter.ExerciseRepositoryAdapter;
import com.community.workout.infra.persistence.adapter.WorkOutLogItemRepositoryAdapter;
import com.community.workout.infra.persistence.adapter.WorkOutLogRepositoryAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkOutLogService {
    private final WorkOutLogRepositoryAdapter workOutLogRepositoryAdapter;
    private final ExerciseRepositoryAdapter exerciseRepositoryAdapter;
    private final WorkOutLogItemRepositoryAdapter workOutLogItemRepositoryAdapter;

    @Transactional(readOnly = true)
    public WorkOutLog getTodayLog(Long memberId, LocalDate date) {
        return workOutLogRepositoryAdapter.findByMemberIdAndLogDate(memberId, date)
                .orElseThrow(() -> new CommonException(ResponseCode.WORKOUT_LOG_NOT_FOUND));
    }

    @Transactional
    public CreateWorkOutLogResult createWorkOutLog(Long memberId, @Valid CreateWorkOutLogRequest request) {
        LocalDate logDate = request.getLogDate();

        boolean exists = workOutLogRepositoryAdapter
                .existsByMemberIdAndLogDateAndDeletedAtIsNull(memberId, logDate);

        if (exists) {
            throw new CommonException(ResponseCode.WORK_OUT_LOG_ALREADY_EXIST);
        }

        WorkOutLog log = WorkOutLog.create(memberId, logDate);

        int seq = 1;

        for (var itemReq: request.getItems()){
            Exercise ex = exerciseRepositoryAdapter.findById(itemReq.getExerciseId())
                    .orElseThrow(()-> new CommonException(ResponseCode.EXERCISE_NOT_FOUND));

            int orderSeq = seq++;

            WorkOutLogItem item = WorkOutLogItem.create(
                    ex,
                    orderSeq,
                    itemReq.getPlannedSets(),
                    itemReq.getPlannedReps(),
                    itemReq.getPlannedRpe(),
                    itemReq.getPlannedRestSec(),
                    itemReq.getNotes(),
                    OffsetDateTime.now()
            );

            log.addItem(item);

        }

        WorkOutLog saved = workOutLogRepositoryAdapter.save(log);
        return CreateWorkOutLogResult.of(saved);
    }

    @Transactional(readOnly = true)
    public WorkOutLogDetailResult getDetailWorkOutLog(Long memberId, Long workOutLogId) {
            WorkOutLog workOutLog = workOutLogRepositoryAdapter.findDetailActive(workOutLogId, memberId)
                    .orElseThrow(()-> new CommonException(ResponseCode.WORKOUT_LOG_NOT_FOUND));

            return WorkOutLogDetailResult.of(workOutLog);
    }

    @Transactional(readOnly = true)
    public WorkOutLogListResult getListWorkOutLog(Long memberId) {
        List<WorkOutLog> workOutLogList = workOutLogRepositoryAdapter.findAllByMemberIdOrderByLogDateDesc(memberId);
        return WorkOutLogListResult.of(workOutLogList);
    }

    @Transactional
    public UpdateWorkOutLogResult updateWorkOutLog(@Valid UpdateWorkOutLogRequest request, Long memberId, Long workOutLogId) {
        WorkOutLog workOutLog = workOutLogRepositoryAdapter.findByIdAndMemberId(workOutLogId, memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.WORKOUT_LOG_NOT_FOUND));

        if (request.getLogDate() == null && request.getStatus() == null) {
            throw new CommonException(ResponseCode.INVALID_REQUEST);
        }
        workOutLog.update(request);

        return UpdateWorkOutLogResult.of(workOutLog);
    }

    @Transactional
    public ReplaceExerciseResult replaceExercise(Long memberId, Long logId, Long itemId, @Valid ReplaceExerciseRequest request) {

        WorkOutLog log = workOutLogRepositoryAdapter.findByIdAndMemberIdAndDeletedAtIsNull(logId, memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.WORKOUT_LOG_NOT_FOUND));

        WorkOutLogItem item = workOutLogItemRepositoryAdapter.findByIdAndLogIdAndDeletedAtIsNull(itemId, log.getId())
                .orElseThrow(() -> new CommonException(ResponseCode.WORKOUT_LOG_ITEM_NOT_FOUND));

        Exercise ex = exerciseRepositoryAdapter.findById(request.getNewExerciseId())
                .orElseThrow(() -> new CommonException(ResponseCode.EXERCISE_NOT_FOUND));

        WorkOutLogItem replacedItem = item.replaceExercise(ex);
        return ReplaceExerciseResult.of(replacedItem);
    }

    @Transactional
    public DeleteWorkOutLogResult deleteWorkOutLog(Long memberId, Long workOutLogId) {
        WorkOutLog log = workOutLogRepositoryAdapter
                .findByIdAndMemberIdAndDeletedAtIsNull(workOutLogId, memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.WORKOUT_LOG_NOT_FOUND));

        log.softDelete();

        return DeleteWorkOutLogResult.of(log);
    }

    @Transactional
    public DeleteWorkOutLogItemResult deleteWorkOutLogItem(Long memberId, Long logId, Long itemId) {
        WorkOutLog log = workOutLogRepositoryAdapter
                .findByIdAndMemberIdAndDeletedAtIsNull(logId, memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.WORKOUT_LOG_NOT_FOUND));

        WorkOutLogItem item = workOutLogItemRepositoryAdapter
                .findByIdAndLogIdAndDeletedAtIsNull(itemId, logId)
                .orElseThrow(() -> new CommonException(ResponseCode.WORKOUT_LOG_ITEM_NOT_FOUND));

        int deletedSeq = item.getOrderSeq();

        item.softDelete();

        workOutLogItemRepositoryAdapter.shiftLeftAfterDelete(logId, deletedSeq);

        return DeleteWorkOutLogItemResult.of(item);
    }
}
