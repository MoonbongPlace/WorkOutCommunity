package com.community.workout.application.dto;

import com.community.workout.domain.model.WorkOutLog;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class WorkOutLogListResult {
    private List<Log> workOutLogList;

    @Getter
    @AllArgsConstructor
    @JsonPropertyOrder({"id", "logDate","createdAt"})
    private static class Log {
        private Long id;
        private LocalDate logDate;
        private OffsetDateTime createdAt;
    }

    public static WorkOutLogListResult of(List<WorkOutLog> workOutLogList) {
        List<Log> mappedLog = workOutLogList.stream().map(
                Log -> new Log(
                        Log.getId(),
                        Log.getLogDate(),
                        Log.getCreatedAt()
                )
        ).toList();

        return new WorkOutLogListResult(
                mappedLog
        );
    }
}
