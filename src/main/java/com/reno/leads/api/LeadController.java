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

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<LeadResponse> create(@Valid @RequestBody LeadCreateRequest request) { return new ApiResponse<>(true, service.create(request), "Lead created successfully"); }
    @GetMapping public ApiResponse<java.util.List<LeadResponse>> list() { return ApiResponse.ok(service.list()); }
    @GetMapping("/{id}") public ApiResponse<LeadResponse> get(@PathVariable Long id) { return ApiResponse.ok(service.get(id)); }
    @PutMapping("/{id}") public ApiResponse<LeadResponse> update(@PathVariable Long id, @Valid @RequestBody LeadUpdateRequest request) { return ApiResponse.ok(service.update(id, request)); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}
