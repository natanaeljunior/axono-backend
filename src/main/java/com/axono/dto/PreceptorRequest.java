package com.axono.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PreceptorRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 255)
        String name,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "CRM é obrigatório")
        @Size(max = 20)
        String crm,

        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String password
) {
    public boolean hasPassword() {
        return password != null && !password.isBlank();
    }
}
