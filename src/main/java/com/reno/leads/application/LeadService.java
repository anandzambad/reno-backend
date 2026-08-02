package com.reno.leads.application;

import com.reno.leads.api.LeadCreateRequest;

public interface LeadService {
    void create(LeadCreateRequest request);
}
