package com.reno.booking;

import com.reno.availability.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class BookingService {
 private final BookingRepository bookings; private final ContractorAvailabilityRepository availability; private final RedisAvailabilityService redis;
 public BookingService(BookingRepository bookings,ContractorAvailabilityRepository availability,RedisAvailabilityService redis){this.bookings=bookings;this.availability=availability;this.redis=redis;}
 public record CreateBooking(Long customerId,Long contractorId,Long serviceId,double latitude,double longitude,String address,Instant scheduledAt,BigDecimal estimatedPrice){}
 @Transactional
 public BookingEntity create(CreateBooking request){
   ContractorAvailabilityEntity a=availability.findForUpdateByContractorId(request.contractorId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Contractor availability not found"));
   if(a.getStatus()!=AvailabilityStatus.AVAILABLE || !redis.isOnline(request.contractorId())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Contractor is no longer available");
   if(bookings.countActiveForContractor(request.contractorId())>0) throw new ResponseStatusException(HttpStatus.CONFLICT,"Contractor already has an active booking");
   BookingEntity b=new BookingEntity(); b.setCustomerId(request.customerId());b.setContractorId(request.contractorId());b.setServiceId(request.serviceId());b.setLatitude(request.latitude());b.setLongitude(request.longitude());b.setAddress(request.address());b.setScheduledAt(request.scheduledAt());b.setEstimatedPrice(request.estimatedPrice());b.setStatus(BookingStatus.CONFIRMED);b.setAcceptedAt(Instant.now());
   BookingEntity saved=bookings.save(b); a.setStatus(AvailabilityStatus.BUSY); availability.save(a); return saved;
 }
 @Transactional
 public BookingEntity updateStatus(long id,BookingStatus target){
   BookingEntity b=bookings.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Booking not found"));
   if(!allowed(b.getStatus(),target)) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Invalid booking transition");
   b.setStatus(target); if(target==BookingStatus.IN_PROGRESS)b.setStartedAt(Instant.now()); if(target==BookingStatus.COMPLETED)b.setCompletedAt(Instant.now()); if(target==BookingStatus.CANCELLED)b.setCancelledAt(Instant.now());
   if(target==BookingStatus.COMPLETED||target==BookingStatus.CANCELLED){ContractorAvailabilityEntity a=availability.findForUpdateByContractorId(b.getContractorId()).orElse(null);if(a!=null){a.setStatus(AvailabilityStatus.AVAILABLE);availability.save(a);}}
   return bookings.save(b);
 }
 private boolean allowed(BookingStatus from,BookingStatus to){return switch(from){case CONFIRMED->to==BookingStatus.EN_ROUTE||to==BookingStatus.CANCELLED;case EN_ROUTE->to==BookingStatus.ARRIVED||to==BookingStatus.CANCELLED;case ARRIVED->to==BookingStatus.IN_PROGRESS||to==BookingStatus.CANCELLED;case IN_PROGRESS->to==BookingStatus.COMPLETED||to==BookingStatus.CANCELLED;default->false;};}
}
