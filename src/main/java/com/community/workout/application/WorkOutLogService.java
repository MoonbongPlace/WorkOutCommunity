package com.community.workout.application;

import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.workout.api.dto.request.SaveRecommendedRoutineRequest;
import com.community.workout.domain.model.Exercise;
import com.community.workout.domain.model.WorkOutLog;
import com.community.workout.domain.model.WorkOutLogItem;
import com.community.workout.infra.persistence.adapter.ExerciseRepositoryAdapter;
import com.community.workout.infra.persistence.adapter.WorkOutLogRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkOutLogService {
    private final WorkOutLogRepositoryAdapter workOutLogRepositoryAdapter;
    private final ExerciseRepositoryAdapter exerciseRepositoryAdapter;

    @Transactional
    public Long saveRecommendedRoutine(Long memberId, LocalDate date, SaveRecommendedRoutineRequest request) {

        // 프로젝트 컨벤션에 맞게 생성 기능 엔티티로 이동
        // 1) 오늘 일지 조회/없으면 생성
        WorkOutLog log = workOutLogRepositoryAdapter.findByMemberIdAndLogDate(memberId, date)
                .orElseGet(() -> WorkOutLog.builder()
                        .memberId(memberId)
                        .logDate(date)
                        .status(WorkOutLog.Status.PLANNED)
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .build()
                );

        // 2) exerciseId들을 한 번에 로드해서 N+1 방지
        List<Long> ids = request.items().stream()
                .map(SaveRecommendedRoutineRequest.Item::exerciseId)
                .distinct()
                .toList();
        List<Exercise> exercises = exerciseRepositoryAdapter.findAllById(ids);

        if (exercises.size() != ids.size()) {
            throw new CommonException(ResponseCode.EXERCISE_NOT_FOUND); // 너희 컨벤션 코드로
        }

        Map<Long, Exercise> exerciseMap = exercises.stream()
                .collect(Collectors.toMap(Exercise::getId, e -> e));

        // 4) (정책) 오늘 일지에 추천 루틴을 덮어쓸지/추가할지 결정 필요
        // v1 권장: "덮어쓰기" 옵션 제공. 여기서는 간단히 덮어쓰기(기존 아이템 삭제)
        log.getItems().clear(); // orphanRemoval=true 전제

        // 5) 아이템 추가
        for (SaveRecommendedRoutineRequest.Item it : request.items()) {
            Exercise ex = exerciseMap.get(it.exerciseId());
            if (ex == null) {
                throw new CommonException(ResponseCode.EXERCISE_NOT_FOUND);
            }

            WorkOutLogItem item = WorkOutLogItem.builder()
                    .exercise(ex)
                    .orderSeq(it.orderSeq())
                    .plannedSets(it.sets())
                    .plannedReps(it.reps())
                    .plannedRpe(it.rpe())
                    .plannedRestSec(it.restSec())
                    .notes(it.notes())
                    .createdAt(OffsetDateTime.now())
                    .updatedAt(OffsetDateTime.now())
                    .build();

            log.addItem(item);
        }

        log.setUpdatedAt(OffsetDateTime.now());

        // 6) 저장 (cascade로 item도 저장)
        WorkOutLog saved = workOutLogRepositoryAdapter.save(log);

        return saved.getId();
    }

    @Transactional(readOnly = true)
    public WorkOutLog getTodayLog(Long memberId, LocalDate date) {
        return workOutLogRepositoryAdapter.findByMemberIdAndLogDate(memberId, date)
                .orElseThrow(() -> new CommonException(ResponseCode.WORKOUT_LOG_NOT_FOUND));
    }
}
