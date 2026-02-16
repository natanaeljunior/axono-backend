package com.axono.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record StudentRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 255)
        String name,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "Matrícula (RA) é obrigatória")
        @Size(max = 50)
        String matricula,

        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String password,

        /** Grupo de internato (opcional). */
        UUID groupId
) {
    public boolean hasPassword() {
        return password != null && !password.isBlank();
    }
}
