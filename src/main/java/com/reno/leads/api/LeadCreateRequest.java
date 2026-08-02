package com.reno.leads.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LeadCreateRequest(
        @NotNull Integer service,
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String mobileNumber,
        @NotBlank String postalCode,
        @NotBlank String description) {
}
