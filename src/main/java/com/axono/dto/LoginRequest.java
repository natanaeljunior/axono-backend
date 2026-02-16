package com.axono.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Identificador é obrigatório") String identifier,
        @NotBlank(message = "Senha é obrigatória") String password
) {}
