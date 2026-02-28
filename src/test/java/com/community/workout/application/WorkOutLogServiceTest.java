//package com.community.workout.application;
//
//import com.community.workout.api.dto.request.CreateWorkOutLogRequest;
//import com.community.workout.application.dto.CreateWorkOutLogResult;
//import com.community.workout.domain.model.Exercise;
//import com.community.workout.domain.model.WorkOutLog;
//import com.community.workout.domain.model.WorkOutLogItem;
//import com.community.workout.infra.persistence.adapter.ExerciseRepositoryAdapter;
//import com.community.workout.infra.persistence.adapter.WorkOutLogRepositoryAdapter;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class WorkOutLogServiceTest {
//
//    @Autowired
//    private WorkOutLogService workOutLogService;
//
//    @Autowired
//    private WorkOutLogRepositoryAdapter workOutLogRepositoryAdapter;
//
//    @Autowired
//    private ExerciseRepositoryAdapter exerciseRepositoryAdapter;
//
//    @Test
//    @DisplayName("운동일지 생성 성공: log + items 저장되고 orderSeq 자동 채움")
//    void createWorkOutLog() {
//        // given
//        Long memberId = 1L;
//
//        // Exercise 선행 데이터 준비 (DB에 존재해야 findById 통과)
//        Exercise ex1 = exerciseRepositoryAdapter.save(Exercise.builder()
//                .name("Squat")
//                .build());
//        Exercise ex2 = exerciseRepositoryAdapter.save(Exercise.builder()
//                .name("Leg Press")
//                .build());
//
//        LocalDate logDate = LocalDate.now().plusDays(1);
//
//        CreateWorkOutLogRequest request = new CreateWorkOutLogRequest(
//                logDate,
//                "test memo",
//                List.of(
//                        new CreateWorkOutLogRequest.ExerciseList(
//                                ex1.getId(),
//                                1,   // orderSeq null -> 자동 1
//                                3,
//                                "8-10",
//                                8,
//                                90,
//                                "note1"
//                        ),
//                        new CreateWorkOutLogRequest.ExerciseList(
//                                ex2.getId(),
//                                2,      // orderSeq 지정 -> 그대로 5
//                                4,
//                                "10-12",
//                                null,
//                                120,
//                                "note2"
//                        )
//                )
//        );
//        // when
//        CreateWorkOutLogResult result = workOutLogService.createWorkOutLog(memberId, request);
//
//        // then (결과 DTO 검증)
//        assertThat(result).isNotNull();
//        assertThat(result.getMemberId()).isEqualTo(memberId);
//        assertThat(result.getLogDate()).isEqualTo(logDate);
//
//        // then (DB 저장 검증: log 존재 + items 존재)
//        WorkOutLog saved = workOutLogRepositoryAdapter.findByMemberIdAndLogDate(memberId, logDate)
//                .orElseThrow();
//
//        assertThat(saved.getItems()).hasSize(2);
//
//        // items orderSeq 검증 (첫번째는 자동 1, 두번째는 지정 5)
//        List<WorkOutLogItem> items = saved.getItems();
//        assertThat(items.get(0).getOrderSeq()).isEqualTo(1);
//        assertThat(items.get(1).getOrderSeq()).isEqualTo(5);
//
//        // Exercise 연관 검증
//        assertThat(items.get(0).getExercise().getId()).isEqualTo(ex1.getId());
//        assertThat(items.get(1).getExercise().getId()).isEqualTo(ex2.getId());
//    }
//
//    @Test
//    void getDetailWorkOutLog() {
//    }
//
//    @Test
//    void getListWorkOutLog() {
//    }
//
//    @Test
//    void updateWorkOutLog() {
//    }
//
//    @Test
//    void replaceExercise() {
//    }
//
//    @Test
//    void deleteWorkOutLog() {
//    }
//
//    @Test
//    void deleteWorkOutLogItem() {
//    }
//}