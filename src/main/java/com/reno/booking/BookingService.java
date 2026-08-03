package com.reno.booking;

import com.reno.availability.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class BookingService {
 private final BookingRepository bookings; private final ContractorAvailabilityRepository availability; private final ContractorServiceRepository contractorServices; private final RedisAvailabilityService redis;
 public BookingService(BookingRepository bookings,ContractorAvailabilityRepository availability,ContractorServiceRepository contractorServices,RedisAvailabilityService redis){this.bookings=bookings;this.availability=availability;this.contractorServices=contractorServices;this.redis=redis;}
 public record CreateBooking(Long customerId,Long contractorId,Long serviceId,double latitude,double longitude,String address,Instant scheduledAt,BigDecimal estimatedPrice){}
 public record AuthenticatedBooking(String subject,Long customerId,Long contractorId,Long serviceId,double latitude,double longitude,String address,Instant scheduledAt,BigDecimal estimatedPrice){}
 @Transactional
 public BookingEntity createForUser(Long customerId,CreateBooking request){
   return create(new CreateBooking(customerId,request.contractorId(),request.serviceId(),request.latitude(),request.longitude(),request.address(),request.scheduledAt(),request.estimatedPrice()));
 }
 @Transactional
 public BookingEntity create(CreateBooking request){
   if(request.scheduledAt()!=null && request.scheduledAt().isBefore(Instant.now())) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Scheduled time must be in the future");
   ContractorAvailabilityEntity a=availability.findForUpdateByContractorId(request.contractorId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Contractor availability not found"));
   if(a.getStatus()!=AvailabilityStatus.AVAILABLE || !redis.isOnline(request.contractorId())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Contractor is no longer available");
   boolean eligible=contractorServices.findByServiceIdAndActiveTrueAndContractorIdIn(request.serviceId(),List.of(request.contractorId())).stream().findAny().isPresent();
   if(!eligible) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Contractor does not provide the requested service");
   if(a.getLatitude()==null||a.getLongitude()==null) throw new ResponseStatusException(HttpStatus.CONFLICT,"Contractor location is currently unavailable");
   double distance=distanceKm(a.getLatitude(),a.getLongitude(),request.latitude(),request.longitude());
   if(distance>a.getServiceRadiusKm()) throw new ResponseStatusException(HttpStatus.CONFLICT,"Contractor is outside the service area");
   if(bookings.countActiveForContractor(request.contractorId())>0) throw new ResponseStatusException(HttpStatus.CONFLICT,"Contractor already has an active booking");
   BookingEntity b=new BookingEntity(); b.setCustomerId(request.customerId());b.setContractorId(request.contractorId());b.setServiceId(request.serviceId());b.setLatitude(request.latitude());b.setLongitude(request.longitude());b.setAddress(request.address());b.setScheduledAt(request.scheduledAt());b.setEstimatedPrice(request.estimatedPrice());b.setStatus(BookingStatus.CONFIRMED);b.setAcceptedAt(Instant.now());
   BookingEntity saved=bookings.save(b); a.setStatus(AvailabilityStatus.BUSY); availability.save(a); redis.markOffline(request.contractorId()); return saved;
 }
 @Transactional
 public BookingEntity updateStatusForUser(long id,BookingStatus target,String subject,String role){
   BookingEntity b=bookings.findForUpdateById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Booking not found"));
   if(!"ROLE_ADMIN".equals(role)&&!"ROLE_CONTRACTOR".equals(role)) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Only contractor or admin can change booking status");
   return updateStatusLocked(b,target);
 }
 @Transactional
 public BookingEntity updateStatus(long id,BookingStatus target){
   BookingEntity b=bookings.findForUpdateById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Booking not found")); return updateStatusLocked(b,target);
 }
 private BookingEntity updateStatusLocked(BookingEntity b,BookingStatus target){
   if(!allowed(b.getStatus(),target)) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Invalid booking transition from "+b.getStatus()+" to "+target);
   b.setStatus(target); if(target==BookingStatus.IN_PROGRESS)b.setStartedAt(Instant.now()); if(target==BookingStatus.COMPLETED)b.setCompletedAt(Instant.now()); if(target==BookingStatus.CANCELLED)b.setCancelledAt(Instant.now());
   if(target==BookingStatus.COMPLETED||target==BookingStatus.CANCELLED){ContractorAvailabilityEntity a=availability.findForUpdateByContractorId(b.getContractorId()).orElse(null);if(a!=null){a.setStatus(AvailabilityStatus.AVAILABLE);availability.save(a);if(a.getLatitude()!=null&&a.getLongitude()!=null)redis.upsertLocation(b.getContractorId(),a.getLatitude(),a.getLongitude());}}
   return bookings.save(b);
 }
 private boolean allowed(BookingStatus from,BookingStatus to){return switch(from){case CONFIRMED->to==BookingStatus.EN_ROUTE||to==BookingStatus.CANCELLED;case EN_ROUTE->to==BookingStatus.ARRIVED||to==BookingStatus.CANCELLED;case ARRIVED->to==BookingStatus.IN_PROGRESS||to==BookingStatus.CANCELLED;case IN_PROGRESS->to==BookingStatus.COMPLETED||to==BookingStatus.CANCELLED;default->false;};}
 private double distanceKm(double lat1,double lon1,double lat2,double lon2){double r=6371.0088;double dLat=Math.toRadians(lat2-lat1),dLon=Math.toRadians(lon2-lon1);double a=Math.sin(dLat/2)*Math.sin(dLat/2)+Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))*Math.sin(dLon/2)*Math.sin(dLon/2);return r*2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));}
}
