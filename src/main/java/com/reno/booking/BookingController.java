package com.reno.booking;

import com.reno.common.api.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.Instant;

@RestController @RequestMapping("/api/v1/bookings")
public class BookingController {
 private final BookingService service;
 public BookingController(BookingService service){this.service=service;}
 public record CreateRequest(@NotNull @Positive Long customerId,@NotNull @Positive Long contractorId,@NotNull @Positive Long serviceId,@DecimalMin("-90") @DecimalMax("90") double latitude,@DecimalMin("-180") @DecimalMax("180") double longitude,String address,Instant scheduledAt,BigDecimal estimatedPrice){}
 public record StatusRequest(@NotNull BookingStatus status){}
 @PostMapping public ApiResponse<BookingEntity> create(@Valid @RequestBody CreateRequest r){return ApiResponse.ok(service.create(new BookingService.CreateBooking(r.customerId(),r.contractorId(),r.serviceId(),r.latitude(),r.longitude(),r.address(),r.scheduledAt(),r.estimatedPrice())));}
 @PatchMapping("/{id}/status") public ApiResponse<BookingEntity> status(@PathVariable @Positive long id,@Valid @RequestBody StatusRequest r){return ApiResponse.ok(service.updateStatus(id,r.status()));}
}
