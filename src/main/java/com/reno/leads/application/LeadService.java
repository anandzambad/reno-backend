package com.reno.leads.application;

import com.reno.leads.api.LeadCreateRequest;
import com.reno.leads.api.LeadResponse;

public interface LeadService {
    void create(LeadCreateRequest request);
    LeadResponse get(Long id);
}
