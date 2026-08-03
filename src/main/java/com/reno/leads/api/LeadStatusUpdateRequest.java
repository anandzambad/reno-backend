package com.reno.leads.api;

import jakarta.validation.constraints.NotNull;

public record LeadStatusUpdateRequest(@NotNull LeadStatus status) {}
