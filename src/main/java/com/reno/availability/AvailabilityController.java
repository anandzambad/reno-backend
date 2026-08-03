package com.reno.availability;

import com.reno.common.api.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/contractors")
public class AvailabilityController {
    private final ContractorAvailabilityRepository repository;
    private final RedisAvailabilityService redis;
    public AvailabilityController(ContractorAvailabilityRepository repository, RedisAvailabilityService redis){this.repository=repository;this.redis=redis;}

    public record AvailabilityRequest(@NotNull AvailabilityStatus status, @DecimalMin("0") @DecimalMax("90") Double latitude, @DecimalMin("-180") @DecimalMax("180") Double longitude, @Positive Double serviceRadiusKm){}
    public record LocationRequest(@DecimalMin("-90") @DecimalMax("90") double latitude, @DecimalMin("-180") @DecimalMax("180") double longitude){}
    public record NearbyContractor(long contractorId,double distanceKm,boolean available){ }

    @PostMapping("/{contractorId}/availability")
    @Transactional
    public ApiResponse<AvailabilityStatus> updateAvailability(@PathVariable long contractorId,@Valid @RequestBody AvailabilityRequest request){
        ContractorAvailabilityEntity entity=repository.findByContractorId(contractorId).orElseGet(()->{ContractorAvailabilityEntity e=new ContractorAvailabilityEntity();e.setContractorId(contractorId);return e;});
        entity.setStatus(request.status()); if(request.serviceRadiusKm()!=null) entity.setServiceRadiusKm(request.serviceRadiusKm());
        if(request.latitude()!=null && request.longitude()!=null){entity.setLatitude(request.latitude());entity.setLongitude(request.longitude());entity.setLastSeenAt(Instant.now());redis.upsertLocation(contractorId,request.latitude(),request.longitude());}
        repository.save(entity);
        if(request.status()==AvailabilityStatus.OFFLINE) redis.markOffline(contractorId);
        return ApiResponse.ok(entity.getStatus());
    }

    @PostMapping("/{contractorId}/location")
    @Transactional
    public ApiResponse<String> updateLocation(@PathVariable long contractorId,@Valid @RequestBody LocationRequest request){
        ContractorAvailabilityEntity entity=repository.findByContractorId(contractorId).orElseGet(()->{ContractorAvailabilityEntity e=new ContractorAvailabilityEntity();e.setContractorId(contractorId);e.setStatus(AvailabilityStatus.AVAILABLE);return e;});
        entity.setLatitude(request.latitude()); entity.setLongitude(request.longitude()); entity.setLastSeenAt(Instant.now()); repository.save(entity); redis.upsertLocation(contractorId,request.latitude(),request.longitude()); return ApiResponse.ok("updated");
    }

    @GetMapping("/nearby")
    public ApiResponse<List<NearbyContractor>> nearby(@RequestParam long serviceId,@RequestParam @DecimalMin("-90") @DecimalMax("90") double latitude,@RequestParam @DecimalMin("-180") @DecimalMax("180") double longitude,@RequestParam(defaultValue="10") @Positive double radiusKm,@RequestParam(defaultValue="20") @Positive int limit){
        List<NearbyContractor> list=redis.nearby(latitude,longitude,Math.min(radiusKm,50),Math.min(limit,50)).stream().filter(x->redis.isOnline(Long.parseLong(x.getContent().getName()))).map(x->new NearbyContractor(Long.parseLong(x.getContent().getName()),x.getDistance().getValue(),true)).toList();
        return ApiResponse.ok(list);
    }
}
