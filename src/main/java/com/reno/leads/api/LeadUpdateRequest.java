package com.reno.leads.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LeadUpdateRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String mobileNumber,
        @NotBlank String postalCode,
        @NotBlank String description,
        @NotBlank String status) {}
