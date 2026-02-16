package com.axono.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GroupRequest(
        @NotBlank(message = "Código do grupo é obrigatório")
        @Size(max = 20)
        String code,

        @Size(max = 20)
        String cycle
) {}
