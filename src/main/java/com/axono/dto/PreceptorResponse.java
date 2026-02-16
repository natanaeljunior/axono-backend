package com.axono.dto;

import com.axono.domain.User;

import java.time.Instant;
import java.util.UUID;

public record PreceptorResponse(
        UUID id,
        String name,
        String email,
        String crm,
        boolean firstAccessPending,
        Instant createdAt
) {
    public static PreceptorResponse from(User user) {
        return new PreceptorResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCrm(),
                user.isFirstAccessPending(),
                user.getCreatedAt()
        );
    }
}
