package com.reno.availability;

import jakarta.persistence.*;

@Entity @Table(name="contractor_services",uniqueConstraints=@UniqueConstraint(name="uk_contractor_service",columnNames={"contractor_id","service_id"}))
public class ContractorServiceEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="contractor_id",nullable=false) private Long contractorId;
 @Column(name="service_id",nullable=false) private Long serviceId;
 @Column(nullable=false) private boolean active=true;
 public Long getId(){return id;} public Long getContractorId(){return contractorId;} public void setContractorId(Long v){contractorId=v;} public Long getServiceId(){return serviceId;} public void setServiceId(Long v){serviceId=v;} public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
