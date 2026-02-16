package com.axono.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record StudentUpdateRequest(
        @Size(max = 255)
        String name,

        @Email(message = "E-mail inválido")
        String email,

        @Size(max = 50)
        String matricula,

        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String password,

        /** Grupo de internato (opcional). null = mantém; enviar UUID = altera; pode usar um sentinel para "remover" se desejar. */
        UUID groupId
) {
    public boolean hasPassword() {
        return password != null && !password.isBlank();
    }
}
