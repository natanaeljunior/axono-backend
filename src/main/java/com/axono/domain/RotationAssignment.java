package com.axono.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Uma célula da matriz de rotações: grupo G no período P (bimestre 1-6)
 * com tipo de rotação, hospital e preceptor (todos opcionais para célula vazia).
 */
@Entity
@Table(name = "rotation_assignments", indexes = {
        @Index(name = "idx_rotation_assignments_group", columnList = "group_id"),
        @Index(name = "uk_rotation_assignments_group_period", columnList = "group_id, period_index", unique = true)
})
@Getter
@Setter
public class RotationAssignment extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    /** Período no ciclo (1 a 6 = bimestres). */
    @NotNull
    @Min(1)
    @Max(6)
    @Column(name = "period_index", nullable = false)
    private Integer periodIndex;

    /** Tipo da rotação (SURGERY, PEDIATRICS, etc.). Pode ser null = célula vazia. */
    @Enumerated(EnumType.STRING)
    @Column(name = "rotation_type", length = 20)
    private RotationType rotationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preceptor_id")
    private User preceptor;
}
