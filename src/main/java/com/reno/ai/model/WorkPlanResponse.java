package com.reno.ai.model;

import java.util.List;

public record WorkPlanResponse(
        String projectCode,
        String summary,
        List<WorkPlanTask> tasks,
        String disclaimer
) {}
