package com.reno.leads.api;

import com.reno.common.api.ApiResponse;
import com.reno.leads.persistence.LeadEntity;
import com.reno.leads.persistence.LeadRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leads")
public class LeadStatusController {
    private final LeadRepository repository;
    public LeadStatusController(LeadRepository repository) { this.repository = repository; }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<LeadStatus>> updateStatus(@PathVariable Long id, @Valid @RequestBody LeadStatusUpdateRequest request) {
        LeadEntity lead = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lead not found"));
        LeadStatus current = LeadStatus.valueOf(lead.getStatus());
        if (!LeadStatusTransition.allowed(current, request.status())) {
            return ResponseEntity.unprocessableEntity().body(ApiResponse.error("Invalid lead status transition: " + current + " -> " + request.status()));
        }
        lead.setStatus(request.status().name());
        repository.save(lead);
        return ResponseEntity.ok(ApiResponse.ok(request.status()));
    }
}
