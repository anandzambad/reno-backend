package com.reno.availability;

import com.reno.common.api.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@RestController @RequestMapping("/api/v1/contractors")
public class AvailabilityController {
 private final ContractorAvailabilityRepository repository; private final ContractorServiceRepository serviceRepository; private final RedisAvailabilityService redis;
 public AvailabilityController(ContractorAvailabilityRepository repository,ContractorServiceRepository serviceRepository,RedisAvailabilityService redis){this.repository=repository;this.serviceRepository=serviceRepository;this.redis=redis;}
 public record AvailabilityRequest(@NotNull AvailabilityStatus status,@DecimalMin("-90") @DecimalMax("90") Double latitude,@DecimalMin("-180") @DecimalMax("180") Double longitude,@DecimalMin("0.5") @DecimalMax("100") Double serviceRadiusKm){}
 public record LocationRequest(@DecimalMin("-90") @DecimalMax("90") double latitude,@DecimalMin("-180") @DecimalMax("180") double longitude){}
 public record NearbyContractor(long contractorId,double latitude,double longitude,double distanceKm,boolean available){}
 @PostMapping("/{contractorId}/availability") @Transactional
 public ApiResponse<AvailabilityStatus> updateAvailability(@PathVariable @Positive long contractorId,@Valid @RequestBody AvailabilityRequest request){
  ContractorAvailabilityEntity entity=repository.findByContractorId(contractorId).orElseGet(()->{ContractorAvailabilityEntity e=new ContractorAvailabilityEntity();e.setContractorId(contractorId);return e;}); entity.setStatus(request.status()); if(request.serviceRadiusKm()!=null)entity.setServiceRadiusKm(request.serviceRadiusKm());
  if(request.latitude()!=null&&request.longitude()!=null){entity.setLatitude(request.latitude());entity.setLongitude(request.longitude());entity.setLastSeenAt(Instant.now());redis.upsertLocation(contractorId,request.latitude(),request.longitude());} repository.save(entity); if(request.status()==AvailabilityStatus.OFFLINE)redis.markOffline(contractorId); return ApiResponse.ok(entity.getStatus());
 }
 @PostMapping("/{contractorId}/location") @Transactional
 public ApiResponse<String> updateLocation(@PathVariable @Positive long contractorId,@Valid @RequestBody LocationRequest request){
  ContractorAvailabilityEntity entity=repository.findByContractorId(contractorId).orElseGet(()->{ContractorAvailabilityEntity e=new ContractorAvailabilityEntity();e.setContractorId(contractorId);e.setStatus(AvailabilityStatus.AVAILABLE);return e;}); entity.setLatitude(request.latitude());entity.setLongitude(request.longitude());entity.setLastSeenAt(Instant.now());repository.save(entity);redis.upsertLocation(contractorId,request.latitude(),request.longitude());return ApiResponse.ok("updated");
 }
 @GetMapping("/nearby")
 public ApiResponse<List<NearbyContractor>> nearby(@RequestParam @Positive long serviceId,@RequestParam @DecimalMin("-90") @DecimalMax("90") double latitude,@RequestParam @DecimalMin("-180") @DecimalMax("180") double longitude,@RequestParam(defaultValue="10") @DecimalMin("0.5") @DecimalMax("50") double radiusKm,@RequestParam(defaultValue="20") @Positive @org.hibernate.validator.constraints.Range(min=1,max=50) int limit){
  var candidates=redis.nearby(latitude,longitude,radiusKm,limit); var ids=candidates.stream().map(x->Long.parseLong(x.getContent().getName())).filter(redis::isOnline).toList(); if(ids.isEmpty())return ApiResponse.ok(List.of());
  Set<Long> eligible=serviceRepository.findByServiceIdAndActiveTrueAndContractorIdIn(serviceId,ids).stream().map(ContractorServiceEntity::getContractorId).collect(java.util.stream.Collectors.toSet());
  List<NearbyContractor> list=candidates.stream().filter(x->eligible.contains(Long.parseLong(x.getContent().getName()))).map(x->{var p=x.getContent().getPoint();return new NearbyContractor(Long.parseLong(x.getContent().getName()),p.getY(),p.getX(),x.getDistance().getValue(),true);}).toList(); return ApiResponse.ok(list);
 }
}
