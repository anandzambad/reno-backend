package com.reno.services.persistence;
import jakarta.persistence.*;
@Entity @Table(name="services")
public class ServiceEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=150) private String name;
 @Column(length=500) private String description;
 @Column(nullable=false) private boolean active=true;
 public Long getId(){return id;} public String getName(){return name;} public String getDescription(){return description;} public boolean isActive(){return active;}
}
