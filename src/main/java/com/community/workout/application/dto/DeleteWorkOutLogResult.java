package com.community.workout.application.dto;

import com.community.workout.domain.model.WorkOutLog;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"workOutLogId","memberId","logDate","deletedAt"})
public class DeleteWorkOutLogResult {
    private Long workOutLogId;
    private Long memberId;
    private LocalDate logDate;
    private OffsetDateTime deletedAt;

    public static DeleteWorkOutLogResult of(WorkOutLog log) {
        return new DeleteWorkOutLogResult(
                log.getId(),
                log.getMemberId(),
                log.getLogDate(),
                log.getDeletedAt()
        );
    }
}
