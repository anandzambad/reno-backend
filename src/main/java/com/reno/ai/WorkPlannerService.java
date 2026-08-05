package com.reno.ai;

import com.reno.ai.model.WorkPlanRequest;
import com.reno.ai.model.WorkPlanResponse;
import com.reno.ai.model.WorkPlanTask;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkPlannerService {
    public WorkPlanResponse generate(WorkPlanRequest request) {
        List<WorkPlanTask> tasks = new ArrayList<>();
        String text = ((request.requirements() == null ? "" : request.requirements()) + " " +
                (request.scope() == null ? "" : request.scope())).toLowerCase();

        if (text.contains("civil") || text.contains("demolition") || text.contains("floor")) {
            tasks.add(new WorkPlanTask("Civil", "Site preparation", "Prepare the work area and complete required civil preparation.", "HIGH", 2, ""));
        }
        if (text.contains("plumb" ) || text.contains("bathroom") || text.contains("kitchen")) {
            tasks.add(new WorkPlanTask("Plumbing", "Plumbing rough-in", "Complete required plumbing work and pressure checks.", "HIGH", 3, "Civil"));
        }
        if (text.contains("electric") || text.contains("wiring") || text.contains("switch")) {
            tasks.add(new WorkPlanTask("Electrical", "Electrical installation", "Complete wiring, DB, switches and basic testing.", "HIGH", 4, "Civil"));
        }
        if (text.contains("paint") || text.contains("painting")) {
            tasks.add(new WorkPlanTask("Painting", "Painting preparation and coats", "Prepare surfaces, apply primer and finish coats.", "NORMAL", 4, "Civil, Electrical"));
        }
        if (text.contains("tile") || text.contains("flooring")) {
            tasks.add(new WorkPlanTask("Flooring", "Floor/tile installation", "Prepare surfaces and install specified flooring or tiles.", "NORMAL", 4, "Civil, Plumbing"));
        }
        if (text.contains("carpentry") || text.contains("modular") || text.contains("wardrobe")) {
            tasks.add(new WorkPlanTask("Carpentry", "Carpentry installation", "Measure, fabricate and install approved carpentry items.", "NORMAL", 5, "Civil"));
        }
        if (tasks.isEmpty()) {
            tasks.add(new WorkPlanTask("Planning", "Site assessment", "Review the project requirements and create the detailed execution checklist.", "HIGH", 1, ""));
        }

        return new WorkPlanResponse(
                request.projectCode(),
                "Draft work plan generated from the project requirements.",
                tasks,
                "AI-assisted plan only. Contractor/project manager must review and approve before tasks are created."
        );
    }
}
