package com.axono.dto;

import com.axono.domain.Group;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GroupResponse(
        UUID id,
        String code,
        String cycle,
        long studentCount,
        Instant createdAt,
        List<RotationAssignmentResponse> assignments
) {
    /** Para listagem (sem carregar os 6 slots). */
    public static GroupResponse from(Group group, long studentCount) {
        return new GroupResponse(
                group.getId(),
                group.getCode(),
                group.getCycle(),
                studentCount,
                group.getCreatedAt(),
                null
        );
    }

    /** Para detalhe (com os 6 slots da matriz). */
    public static GroupResponse from(Group group, long studentCount, List<RotationAssignmentResponse> assignments) {
        return new GroupResponse(
                group.getId(),
                group.getCode(),
                group.getCycle(),
                studentCount,
                group.getCreatedAt(),
                assignments
        );
    }
}
