package com.reno.leads.application;

import com.reno.leads.api.LeadCreateRequest;
import com.reno.leads.api.LeadResponse;
import com.reno.leads.persistence.LeadEntity;
import com.reno.leads.persistence.LeadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeadServiceImpl implements LeadService {
    private final LeadRepository repository;

    public LeadServiceImpl(LeadRepository repository) { this.repository = repository; }

    @Override
    @Transactional
    public void create(LeadCreateRequest request) {
        LeadEntity entity = new LeadEntity();
        entity.setServiceId(request.service().longValue());
        entity.setName(request.name());
        entity.setEmail(request.email());
        entity.setMobileNumber(request.mobileNumber());
        entity.setPostalCode(request.postalCode());
        entity.setDescription(request.description());
        repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public LeadResponse get(Long id) {
        LeadEntity e = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Lead not found: " + id));
        return new LeadResponse(e.getId(), e.getServiceId(), e.getName(), e.getEmail(), e.getMobileNumber(), e.getPostalCode(), e.getDescription(), e.getStatus());
    }
}
