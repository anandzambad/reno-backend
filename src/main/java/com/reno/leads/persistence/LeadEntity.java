package com.reno.leads.persistence;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "leads")
public class LeadEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "service_id", nullable = false) private Integer serviceId;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String email;
    @Column(name = "mobile_number", nullable = false) private String mobileNumber;
    @Column(name = "postal_code", nullable = false) private String postalCode;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(nullable = false) private String status = "NEW";
    @Column(nullable = false) private String source = "WEB";
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;

    @PrePersist void onCreate() { createdAt = Instant.now(); updatedAt = createdAt; }
    @PreUpdate void onUpdate() { updatedAt = Instant.now(); }

    public Long getId() { return id; }
    public Integer getServiceId() { return serviceId; }
    public void setServiceId(Integer serviceId) { this.serviceId = serviceId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public String getCreatedAtString() { return createdAt == null ? null : createdAt.toString(); }
}
