package com.reno.ai.model;

import jakarta.validation.constraints.NotBlank;

public record WorkPlanRequest(
        @NotBlank String projectCode,
        @NotBlank String projectTitle,
        String propertyType,
        String requirements,
        String scope
) {}
