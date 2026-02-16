package com.axono.dto;

import com.axono.domain.RotationAssignment;

import java.util.UUID;

public record RotationAssignmentResponse(
        UUID id,
        UUID groupId,
        int periodIndex,
        String rotationType,
        UUID hospitalId,
        String hospitalName,
        UUID preceptorId,
        String preceptorName
) {
    public static RotationAssignmentResponse from(RotationAssignment a) {
        return new RotationAssignmentResponse(
                a.getId(),
                a.getGroup().getId(),
                a.getPeriodIndex(),
                a.getRotationType() != null ? a.getRotationType().name() : null,
                a.getHospital() != null ? a.getHospital().getId() : null,
                a.getHospital() != null ? a.getHospital().getName() : null,
                a.getPreceptor() != null ? a.getPreceptor().getId() : null,
                a.getPreceptor() != null ? a.getPreceptor().getName() : null
        );
    }
}
