package com.community.workout.application;

import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.workout.api.dto.request.CreateWorkOutLogRequest;
import com.community.workout.domain.model.Exercise;
import com.community.workout.domain.model.WorkOutLog;
import com.community.workout.domain.model.WorkOutLogItem;
import com.community.workout.infra.persistence.adapter.ExerciseRepositoryAdapter;
import com.community.workout.infra.persistence.adapter.WorkOutLogRepositoryAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class WorkOutLogService {
    private final WorkOutLogRepositoryAdapter workOutLogRepositoryAdapter;
    private final ExerciseRepositoryAdapter exerciseRepositoryAdapter;

    @Transactional(readOnly = true)
    public WorkOutLog getTodayLog(Long memberId, LocalDate date) {
        return workOutLogRepositoryAdapter.findByMemberIdAndLogDate(memberId, date)
                .orElseThrow(() -> new CommonException(ResponseCode.WORKOUT_LOG_NOT_FOUND));
    }

    @Transactional
    public CreateWorkOutLogResult createWorkOutLog(Long memberId, @Valid CreateWorkOutLogRequest request) {
        LocalDate logDate = request.getLogDate();

        workOutLogRepositoryAdapter.findByMemberIdAndLogDate(memberId, logDate)
                .ifPresent(x-> { throw new CommonException(ResponseCode.WORK_OUT_LOG_ALREADY_EXIST); });

        WorkOutLog log = WorkOutLog.create(memberId, logDate);

        int seq = 1;

        for (var itemReq: request.getItems()){
            Exercise ex = exerciseRepositoryAdapter.findById(itemReq.getExerciseId())
                    .orElseThrow(()-> new CommonException(ResponseCode.EXERCISE_NOT_FOUND));

            int orderSeq = itemReq.getOrderSeq() != null ? itemReq.getOrderSeq() : seq++;

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

    public WorkOutLogDetailResult getDetailWorkOutLog(Long memberId, Long workOutLogId) {

            return null;
    }
}
