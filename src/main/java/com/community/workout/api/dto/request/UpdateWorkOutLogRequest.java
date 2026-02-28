package com.community.workout.api.dto.request;

import com.community.workout.domain.model.WorkOutLog;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class UpdateWorkOutLogRequest {
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotBlank(message = "날짜는 입력해야합니다.")
    private LocalDate logDate;
    private WorkOutLog.Status status;
}
