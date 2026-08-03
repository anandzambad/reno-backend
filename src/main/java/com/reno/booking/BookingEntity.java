package com.reno.booking;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity @Table(name="bookings", indexes={@Index(name="idx_bookings_contractor_status",columnList="contractor_id,status"),@Index(name="idx_bookings_customer_created",columnList="customer_id,created_at")})
public class BookingEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="customer_id",nullable=false) private Long customerId; @Column(name="contractor_id",nullable=false) private Long contractorId; @Column(name="service_id",nullable=false) private Long serviceId;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private BookingStatus status;
 @Column(nullable=false,precision=10,scale=7) private Double latitude; @Column(nullable=false,precision=10,scale=7) private Double longitude;
 @Column(length=500) private String address; @Column(name="scheduled_at") private Instant scheduledAt; @Column(name="estimated_price",precision=12,scale=2) private BigDecimal estimatedPrice;
 @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt; @Column(name="accepted_at") private Instant acceptedAt; @Column(name="started_at") private Instant startedAt; @Column(name="completed_at") private Instant completedAt; @Column(name="cancelled_at") private Instant cancelledAt;
 @Version @Column(nullable=false) private Long version;
 @PrePersist void create(){createdAt=Instant.now();}
 public Long getId(){return id;} public Long getCustomerId(){return customerId;} public void setCustomerId(Long v){customerId=v;} public Long getContractorId(){return contractorId;} public void setContractorId(Long v){contractorId=v;} public Long getServiceId(){return serviceId;} public void setServiceId(Long v){serviceId=v;} public BookingStatus getStatus(){return status;} public void setStatus(BookingStatus v){status=v;} public Double getLatitude(){return latitude;} public void setLatitude(Double v){latitude=v;} public Double getLongitude(){return longitude;} public void setLongitude(Double v){longitude=v;} public String getAddress(){return address;} public void setAddress(String v){address=v;} public Instant getScheduledAt(){return scheduledAt;} public void setScheduledAt(Instant v){scheduledAt=v;} public BigDecimal getEstimatedPrice(){return estimatedPrice;} public void setEstimatedPrice(BigDecimal v){estimatedPrice=v;} public Instant getCreatedAt(){return createdAt;} public Instant getAcceptedAt(){return acceptedAt;} public void setAcceptedAt(Instant v){acceptedAt=v;} public Instant getStartedAt(){return startedAt;} public void setStartedAt(Instant v){startedAt=v;} public Instant getCompletedAt(){return completedAt;} public void setCompletedAt(Instant v){completedAt=v;} public Instant getCancelledAt(){return cancelledAt;} public void setCancelledAt(Instant v){cancelledAt=v;}
}
