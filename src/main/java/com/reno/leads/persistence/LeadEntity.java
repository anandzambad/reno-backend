package com.reno.leads.persistence;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "leads", indexes = {
        @Index(name = "idx_leads_status_created", columnList = "status,created_at"),
        @Index(name = "idx_leads_postal_code", columnList = "postal_code")
})
public class LeadEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "service_id", nullable = false) private Long serviceId;
    @Column(nullable = false, length = 120) private String name;
    @Column(nullable = false, length = 180) private String email;
    @Column(name = "mobile_number", nullable = false, length = 30) private String mobileNumber;
    @Column(name = "postal_code", nullable = false, length = 20) private String postalCode;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(nullable = false, length = 30) private String status = "NEW";
    @Column(nullable = false, length = 30) private String source = "WEB";
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;

    @PrePersist void onCreate() { createdAt = Instant.now(); updatedAt = createdAt; }
    @PreUpdate void onUpdate() { updatedAt = Instant.now(); }

    public Long getId() { return id; }
    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
    public String getName() { return name; }
    public void setName(String value) { this.name = value.trim(); }
    public String getEmail() { return email; }
    public void setEmail(String value) { this.email = value.trim().toLowerCase(); }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String value) { this.mobileNumber = value.trim(); }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String value) { this.postalCode = value.trim(); }
    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value == null ? null : value.trim(); }
    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value.trim().toUpperCase(); }
}
