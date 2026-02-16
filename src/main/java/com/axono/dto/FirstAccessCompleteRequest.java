package com.axono.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request para conclusão do primeiro acesso: definir nova senha.
 * Regras: mínimo 8 caracteres, pelo menos uma maiúscula, pelo menos um número ou caractere especial.
 */
public record FirstAccessCompleteRequest(
        @NotBlank(message = "Nova senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String newPassword
) {}
