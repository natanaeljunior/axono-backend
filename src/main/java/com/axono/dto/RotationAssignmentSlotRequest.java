package com.axono.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/** Um slot da matriz (período 1-6) para atualização em lote. */
public record RotationAssignmentSlotRequest(
        @NotNull @Min(1) @Max(6) Integer periodIndex,
        String rotationType,
        UUID hospitalId,
        UUID preceptorId
) {}
