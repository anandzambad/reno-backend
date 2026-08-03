package com.reno.availability;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="contractor_availability", indexes={
    @Index(name="idx_contractor_availability_status", columnList="status"),
    @Index(name="idx_contractor_availability_last_seen", columnList="last_seen_at")
})
public class ContractorAvailabilityEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="contractor_id", nullable=false, unique=true) private Long contractorId;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private AvailabilityStatus status=AvailabilityStatus.OFFLINE;
    @Column(precision=10, scale=7) private Double latitude;
    @Column(precision=10, scale=7) private Double longitude;
    @Column(name="service_radius_km", nullable=false, precision=6, scale=2) private Double serviceRadiusKm=10.0;
    @Column(name="last_seen_at") private Instant lastSeenAt;
    @Version @Column(nullable=false) private Long version;
    @Column(name="updated_at", nullable=false) private Instant updatedAt;
    @PrePersist void create(){updatedAt=Instant.now();}
    @PreUpdate void update(){updatedAt=Instant.now();}
    public Long getId(){return id;} public Long getContractorId(){return contractorId;} public void setContractorId(Long v){contractorId=v;}
    public AvailabilityStatus getStatus(){return status;} public void setStatus(AvailabilityStatus v){status=v;}
    public Double getLatitude(){return latitude;} public void setLatitude(Double v){latitude=v;}
    public Double getLongitude(){return longitude;} public void setLongitude(Double v){longitude=v;}
    public Double getServiceRadiusKm(){return serviceRadiusKm;} public void setServiceRadiusKm(Double v){serviceRadiusKm=v;}
    public Instant getLastSeenAt(){return lastSeenAt;} public void setLastSeenAt(Instant v){lastSeenAt=v;}
}
