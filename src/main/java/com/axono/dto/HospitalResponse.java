package com.axono.dto;

import com.axono.domain.Hospital;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record HospitalResponse(
        UUID id,
        String name,
        String address,
        String status,
        String directorName,
        String directorEmail,
        Integer capacity,
        LocalDate conventionExpiresAt,
        String tag,
        Instant createdAt
) {
    public static HospitalResponse from(Hospital h) {
        return new HospitalResponse(
                h.getId(),
                h.getName(),
                h.getAddress(),
                h.getStatus() != null ? h.getStatus().name() : null,
                h.getDirectorName(),
                h.getDirectorEmail(),
                h.getCapacity(),
                h.getConventionExpiresAt(),
                h.getTag(),
                h.getCreatedAt()
        );
    }
}
