package com.reno.leads.api;

import com.reno.common.api.ApiResponse;
import com.reno.leads.application.LeadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leads")
public class LeadController {
    private final LeadService service;

    public LeadController(LeadService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> create(@Valid @RequestBody LeadCreateRequest request) {
        service.create(request);
        return new ApiResponse<>(true, null, "Lead created successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<LeadResponse> get(@PathVariable Long id) {
        return ApiResponse.ok(service.get(id));
    }
}
