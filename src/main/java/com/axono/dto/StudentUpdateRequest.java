package com.axono.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record StudentUpdateRequest(
        @Size(max = 255)
        String name,

        @Email(message = "E-mail inválido")
        String email,

        @Size(max = 50)
        String matricula,

        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String password
) {
    public boolean hasPassword() {
        return password != null && !password.isBlank();
    }
}
