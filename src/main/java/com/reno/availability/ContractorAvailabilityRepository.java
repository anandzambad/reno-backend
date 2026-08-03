package com.reno.availability;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import java.util.Optional;

public interface ContractorAvailabilityRepository extends JpaRepository<ContractorAvailabilityEntity, Long> {
    Optional<ContractorAvailabilityEntity> findByContractorId(Long contractorId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ContractorAvailabilityEntity> findForUpdateByContractorId(Long contractorId);
}
