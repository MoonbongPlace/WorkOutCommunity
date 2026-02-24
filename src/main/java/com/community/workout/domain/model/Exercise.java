package com.community.workout.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "exercise")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /**
     * 나중에 Converter로 List<String> 매핑
     */
    @Column(name = "body_parts", nullable = false, columnDefinition = "jsonb")
    private String bodyPartsJson;

    @Column(name = "equipment_tags", nullable = false, columnDefinition = "jsonb")
    private String equipmentTagsJson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public enum Difficulty { easy, medium, hard }
}