package com.axono.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record HospitalRequest(
        @NotBlank(message = "Nome do hospital é obrigatório")
        @Size(max = 255)
        String name,

        @Size(max = 500)
        String address,

        /** ACTIVE, EXPIRING, INACTIVE */
        String status,

        @Size(max = 255)
        String directorName,

        @Size(max = 255)
        String directorEmail,

        Integer capacity,

        java.time.LocalDate conventionExpiresAt,

        @Size(max = 100)
        String tag
) {}
