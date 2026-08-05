package com.reno.ai;

import com.reno.ai.model.WorkPlanRequest;
import com.reno.ai.model.WorkPlanResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/work-plans")
public class WorkPlannerController {
    private final WorkPlannerService service;

    public WorkPlannerController(WorkPlannerService service) {
        this.service = service;
    }

    @PostMapping("/draft")
    public ResponseEntity<WorkPlanResponse> generateDraft(@Valid @RequestBody WorkPlanRequest request) {
        return ResponseEntity.ok(service.generate(request));
    }
}
