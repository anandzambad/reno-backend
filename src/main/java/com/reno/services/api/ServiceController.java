package com.reno.services.api;
import com.reno.common.api.ApiResponse;
import com.reno.services.persistence.ServiceRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/v1/services")
public class ServiceController {
 private final ServiceRepository repository;
 public ServiceController(ServiceRepository repository){this.repository=repository;}
 @GetMapping public ApiResponse<List<ServiceResponse>> list(){return ApiResponse.ok(repository.findByActiveTrueOrderByNameAsc().stream().map(s->new ServiceResponse(s.getId(),s.getName(),s.getDescription())).toList());}
}
