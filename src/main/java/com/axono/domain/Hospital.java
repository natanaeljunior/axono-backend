package com.axono.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Unidade de estágio (hospital / clínica / UBS).
 * Usada nas alocações da matriz de rotações (RotationAssignment).
 */
@Entity
@Table(name = "hospitals", indexes = {
        @Index(name = "idx_hospitals_name", columnList = "name")
})
@Getter
@Setter
public class Hospital extends BaseEntity {

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String name;

    @Size(max = 500)
    @Column(length = 500)
    private String address;

    /** ACTIVE, EXPIRING, etc. */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private HospitalStatus status = HospitalStatus.ACTIVE;

    @Size(max = 255)
    @Column(name = "director_name", length = 255)
    private String directorName;

    @Size(max = 255)
    @Column(name = "director_email", length = 255)
    private String directorEmail;

    /** Capacidade total de alunos (vagas). */
    @Column(name = "capacity")
    private Integer capacity;

    /** Data de vencimento do convênio. */
    @Column(name = "convention_expires_at")
    private LocalDate conventionExpiresAt;

    @Size(max = 100)
    @Column(name = "tag", length = 100)
    private String tag;

    public enum HospitalStatus {
        ACTIVE,
        EXPIRING,
        INACTIVE
    }
}
