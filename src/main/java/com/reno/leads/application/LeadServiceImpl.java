package com.reno.leads.application;

import com.reno.common.api.ResourceNotFoundException;
import com.reno.leads.api.LeadCreateRequest;
import com.reno.leads.api.LeadResponse;
import com.reno.leads.api.LeadUpdateRequest;
import com.reno.leads.persistence.LeadEntity;
import com.reno.leads.persistence.LeadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class LeadServiceImpl implements LeadService {
    private static final List<String> STATUSES = List.of("NEW", "REVIEWED", "ASSIGNED", "IN_PROGRESS", "COMPLETED", "CANCELLED");
    private final LeadRepository repository;
    public LeadServiceImpl(LeadRepository repository) { this.repository = repository; }
    @Override @Transactional public LeadResponse create(LeadCreateRequest request) { LeadEntity e=new LeadEntity(); e.setServiceId(request.service().longValue()); e.setName(request.name()); e.setEmail(request.email()); e.setMobileNumber(request.mobileNumber()); e.setPostalCode(request.postalCode()); e.setDescription(request.description()); return toResponse(repository.save(e)); }
    @Override @Transactional(readOnly=true) public LeadResponse get(Long id) { return toResponse(find(id)); }
    @Override @Transactional(readOnly=true) public List<LeadResponse> list() { return repository.findTop100ByOrderByCreatedAtDesc().stream().map(this::toResponse).toList(); }
    @Override @Transactional public LeadResponse update(Long id, LeadUpdateRequest request) { LeadEntity e=find(id); String status=request.status().trim().toUpperCase(); if(!STATUSES.contains(status)) throw new IllegalArgumentException("Unsupported lead status: "+status); e.setName(request.name()); e.setEmail(request.email()); e.setMobileNumber(request.mobileNumber()); e.setPostalCode(request.postalCode()); e.setDescription(request.description()); e.setStatus(status); return toResponse(repository.save(e)); }
    @Override @Transactional public void delete(Long id) { repository.delete(find(id)); }
    private LeadEntity find(Long id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lead not found: "+id)); }
    private LeadResponse toResponse(LeadEntity e) { return new LeadResponse(e.getId(),e.getServiceId(),e.getName(),e.getEmail(),e.getMobileNumber(),e.getPostalCode(),e.getDescription(),e.getStatus()); }
}
