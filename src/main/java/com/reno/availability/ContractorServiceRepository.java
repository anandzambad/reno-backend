package com.reno.availability;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface ContractorServiceRepository extends JpaRepository<ContractorServiceEntity,Long> {
 List<ContractorServiceEntity> findByServiceIdAndActiveTrueAndContractorIdIn(Long serviceId, Collection<Long> contractorIds);
}
