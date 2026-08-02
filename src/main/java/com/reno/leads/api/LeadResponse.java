package com.reno.leads.api;

public record LeadResponse(Long id, Long service, String name, String email, String mobileNumber, String postalCode, String description, String status) {}
