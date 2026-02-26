package com.community.workout.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "work_out_log",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_work_out_log_member_date", columnNames = {"member_id", "log_date"})
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class WorkOutLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Member 엔티티 ManyToOne으로
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderSeq ASC")
    private final List<WorkOutLogItem> items = new ArrayList<>();

    public enum Status { PLANNED, DONE }

    public void addItem(WorkOutLogItem item) {
        items.add(item);
        item.attach(this);
    }
}