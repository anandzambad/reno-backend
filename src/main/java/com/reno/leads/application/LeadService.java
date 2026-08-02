package com.reno.leads.application;

import com.reno.leads.api.LeadCreateRequest;
import com.reno.leads.api.LeadResponse;
import com.reno.leads.api.LeadUpdateRequest;
import java.util.List;

public interface LeadService {
    LeadResponse create(LeadCreateRequest request);
    LeadResponse get(Long id);
    List<LeadResponse> list();
    LeadResponse update(Long id, LeadUpdateRequest request);
    void delete(Long id);
}
