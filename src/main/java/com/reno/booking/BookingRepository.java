package com.reno.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingEntity,Long> {
 @Query("select count(b) from BookingEntity b where b.contractorId=:contractorId and b.status in (com.reno.booking.BookingStatus.ACCEPTED,com.reno.booking.BookingStatus.CONFIRMED,com.reno.booking.BookingStatus.EN_ROUTE,com.reno.booking.BookingStatus.ARRIVED,com.reno.booking.BookingStatus.IN_PROGRESS)") long countActiveForContractor(Long contractorId);
 Optional<BookingEntity> findByIdAndCustomerId(Long id,Long customerId);
}
