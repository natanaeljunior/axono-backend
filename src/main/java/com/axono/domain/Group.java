package com.axono.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Grupo de internato: identificado por código e ciclo.
 * Detalhes por período (bimestre 1-6) ficam em RotationAssignment.
 */
@Entity
@Table(name = "\"groups\"", indexes = {
        @Index(name = "idx_groups_cycle_code", columnList = "cycle, code", unique = true)
})
@Getter
@Setter
public class Group extends BaseEntity {

    /** Código do grupo (ex: "01", "G-01"). Único por ciclo. */
    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String code;

    /** Ciclo acadêmico (ex: "2024.2"). */
    @Size(max = 20)
    @Column(length = 20)
    private String cycle;

    /** Alocações da matriz: um slot por período (1-6). Preceptor por período fica em RotationAssignment. */
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RotationAssignment> assignments = new ArrayList<>();
}
