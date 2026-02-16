package com.axono.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record HospitalUpdateRequest(
        @Size(max = 255)
        String name,

        @Size(max = 500)
        String address,

        String status,

        @Size(max = 255)
        String directorName,

        @Size(max = 255)
        String directorEmail,

        Integer capacity,

        LocalDate conventionExpiresAt,

        @Size(max = 100)
        String tag
) {}
