package com.reno.booking;

import com.reno.common.api.ApiResponse;
import com.reno.security.CurrentUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.Instant;

@RestController @RequestMapping("/api/v1/bookings")
public class BookingController {
 private final BookingService service;
 public BookingController(BookingService service){this.service=service;}
 public record CreateRequest(@NotNull @Positive Long contractorId,@NotNull @Positive Long serviceId,@DecimalMin("-90") @DecimalMax("90") double latitude,@DecimalMin("-180") @DecimalMax("180") double longitude,@Size(max=500) String address,Instant scheduledAt,@DecimalMin("0.0") @DecimalMax("1000000.0") BigDecimal estimatedPrice){}
 public record StatusRequest(@NotNull BookingStatus status){}
 @PostMapping public ApiResponse<BookingEntity> create(@Valid @RequestBody CreateRequest r,Authentication authentication){return ApiResponse.ok(service.createForUser(CurrentUser.subject(authentication),new BookingService.CreateBooking(r.contractorId(),r.serviceId(),r.latitude(),r.longitude(),r.address(),r.scheduledAt(),r.estimatedPrice())));}
 @PatchMapping("/{id}/status") public ApiResponse<BookingEntity> status(@PathVariable @Positive long id,@Valid @RequestBody StatusRequest r,Authentication authentication){return ApiResponse.ok(service.updateStatusForUser(id,r.status(),CurrentUser.subject(authentication),CurrentUser.role(authentication)));}
}
