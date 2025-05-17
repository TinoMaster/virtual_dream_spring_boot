package com.tinomaster.virtualdream.virtualdream.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tinomaster.virtualdream.virtualdream.dtos.ServiceDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualdream.entities.ServiceEntity;
import com.tinomaster.virtualdream.virtualdream.services.ServiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;
    private final ModelMapper mapper;

    private ServiceDto serviceToServiceDto(ServiceEntity service) {
        return mapper.map(service, ServiceDto.class);
    }

    @GetMapping("/private/service/list/{id}")
    public ResponseEntity<ResponseBody<List<ServiceDto>>> getAllServicesByBusinessId(@PathVariable Long id) {
        var serviceList = StreamSupport.stream(serviceService.getAllServiceByBusinessId(id).spliterator(), false)
                .toList();
        List<ServiceDto> services = serviceList.stream().map(this::serviceToServiceDto).collect(Collectors.toList());
        return ResponseType.ok("successfullyRequest", services);
    }

    @PostMapping("/admin/service")
    public ResponseEntity<ResponseBody<ServiceDto>> saveService(@RequestBody ServiceDto serviceDto) {
        ServiceDto serviceSaved = serviceToServiceDto(serviceService.saveService(serviceDto));
        return ResponseType.ok("successfullySaved", serviceSaved);
    }

    @PutMapping("/admin/service/{id}")
    public ResponseEntity<ResponseBody<ServiceDto>> updateService(@PathVariable Long id, @RequestBody ServiceDto serviceDto) {
        try {
            ServiceDto service = mapper.map(serviceService.updateService(id, serviceDto), ServiceDto.class);
            return ResponseType.ok("successfullyUpdated", service);
        } catch (Exception e) {
            return ResponseType.internalServerError(e.getMessage(), null);
        }
    }

    @DeleteMapping("/admin/service/{id}")
    public ResponseEntity<ResponseBody<Object>> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseType.ok("successfullyDeleted");
    }

}
