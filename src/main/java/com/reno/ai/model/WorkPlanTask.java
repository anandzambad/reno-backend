package com.reno.ai.model;

public record WorkPlanTask(
        String stage,
        String title,
        String description,
        String priority,
        Integer estimatedDays,
        String dependsOn
) {}
