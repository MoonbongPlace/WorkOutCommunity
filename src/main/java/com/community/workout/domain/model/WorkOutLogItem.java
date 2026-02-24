package com.community.workout.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "work_out_log_item",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_work_out_log_item_log_order", columnNames = {"log_id", "order_seq"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class WorkOutLogItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "log_id", nullable = false)
    private WorkOutLog log;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "order_seq", nullable = false)
    private int orderSeq;

    @Column(name = "planned_sets", nullable = false)
    private int plannedSets;

    @Column(name = "planned_reps", nullable = false, length = 20)
    private String plannedReps;

    @Column(name = "planned_rpe")
    private Integer plannedRpe;

    @Column(name = "planned_rest_sec")
    private Integer plannedRestSec;

    @Column(columnDefinition = "text")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    void attach(WorkOutLog log) {
        this.log = log;
    }
}