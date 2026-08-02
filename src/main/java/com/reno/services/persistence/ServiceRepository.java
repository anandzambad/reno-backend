package com.reno.services.persistence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ServiceRepository extends JpaRepository<ServiceEntity,Long>{ List<ServiceEntity> findByActiveTrueOrderByNameAsc(); }
