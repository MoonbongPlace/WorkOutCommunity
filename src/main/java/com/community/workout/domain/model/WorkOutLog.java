package com.community.workout.domain.model;

import com.community.workout.api.dto.request.UpdateWorkOutLogRequest;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "work_out_log"
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class WorkOutLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false, updatable = false)
    private Long memberId;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

//    @Column(columnDefinition = "text")
//    private String memo;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderSeq ASC")
    private final List<WorkOutLogItem> items = new ArrayList<>();

    public void softDelete() {
        if (this.deletedAt != null) return;
        this.deletedAt = OffsetDateTime.now();
        for (WorkOutLogItem item : this.items) {
            item.softDelete();
        }
    }

    public enum Status { PLANNED, DONE }

    public static WorkOutLog create(Long memberId, LocalDate logDate) {
        WorkOutLog log = new WorkOutLog();
        log.memberId = memberId;
        log.logDate = logDate;
        log.status = Status.PLANNED;
        log.createdAt = OffsetDateTime.now();
//        log.memo = memo;
        return log;
    }

    public void update(UpdateWorkOutLogRequest request) {
        if (request.getLogDate() != null) {
            this.logDate = request.getLogDate();
        }
        if (request.getStatus() != null) {
            this.status = request.getStatus();
        }
    }

    public void addItem(WorkOutLogItem item) {
        items.add(item);
        item.attach(this);
    }

//    public void updateMemo(String memo) {
//        this.memo = memo;
//    }

    public void markDone() {
        this.status = Status.DONE;
    }
}