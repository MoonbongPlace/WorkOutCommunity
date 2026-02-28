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

    @Column(name = "planned_sets")
    private int plannedSets;

    @Column(name = "planned_reps", length = 20)
    private String plannedReps;

    @Column(name = "planned_rpe")
    private Integer plannedRpe;

    @Column(name = "planned_rest_sec")
    private Integer plannedRestSec;

    @Column(columnDefinition = "text")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    public static WorkOutLogItem create(
            Exercise exercise,
            int orderSeq,
            int sets,
            String reps,
            Integer rpe,
            Integer restSec,
            String notes,
            OffsetDateTime createdAt
    ) {
        if (exercise == null) throw new IllegalArgumentException("exercise is required");
        if (orderSeq <= 0) throw new IllegalArgumentException("orderSeq must be positive");

        WorkOutLogItem item = new WorkOutLogItem();
        item.exercise = exercise;
        item.orderSeq = orderSeq;
        item.plannedSets = sets;
        item.plannedReps = reps;
        item.plannedRpe = rpe;
        item.plannedRestSec = restSec;
        item.notes = notes;
        item.createdAt = createdAt;
        return item;
    }

    public WorkOutLogItem replaceExercise(Exercise newExercise) {
        if (newExercise == null) throw new IllegalArgumentException("exercise is required");

        this.exercise = newExercise;

        this.plannedSets = 0;
        this.plannedReps = null;
        this.plannedRpe = null;
        this.plannedRestSec = null;
        this.notes = null;
        this.updatedAt = OffsetDateTime.now();

        return this;
    }

    void attach(WorkOutLog log) {
        this.log = log;
    }

    public void softDelete() {
        this.deletedAt = OffsetDateTime.now();
    }
}