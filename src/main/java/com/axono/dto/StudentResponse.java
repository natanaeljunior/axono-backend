package com.axono.dto;

import com.axono.domain.User;

import java.time.Instant;
import java.util.UUID;

public record StudentResponse(
        UUID id,
        String name,
        String email,
        String matricula,
        UUID groupId,
        String groupName,
        boolean firstAccessPending,
        Instant createdAt
) {
    public static StudentResponse from(User user) {
        return new StudentResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getMatricula(),
                user.getGroup() != null ? user.getGroup().getId() : null,
                user.getGroup() != null ? user.getGroup().getCode() : null,
                user.isFirstAccessPending(),
                user.getCreatedAt()
        );
    }
}
