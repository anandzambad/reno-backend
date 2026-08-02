package com.reno.leads.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadRepository extends JpaRepository<LeadEntity, Long> {
}
