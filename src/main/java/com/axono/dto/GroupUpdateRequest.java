package com.axono.dto;

import jakarta.validation.constraints.Size;

public record GroupUpdateRequest(
        @Size(max = 20)
        String code,

        @Size(max = 20)
        String cycle
) {}
