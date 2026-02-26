package com.community.workout.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkOutLogRequest {

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate logDate;
//    private String memo;

    @NotEmpty
    List<ExerciseList> items;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExerciseList{

        @NotNull
        private Long exerciseId;
        private Integer orderSeq;

        @Min(1)
        private int plannedSets;

        @NotBlank
        private String plannedReps;
        private Integer plannedRpe;

        private Integer plannedRestSec;
        private String notes;
    }
}
