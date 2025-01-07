package com.tinomaster.virtualdream.virtualDream.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tinomaster.virtualdream.virtualDream.dtos.ServiceDto;
import com.tinomaster.virtualdream.virtualDream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualDream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualDream.entities.ServiceEntity;
import com.tinomaster.virtualdream.virtualDream.services.ServiceService;

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

	@DeleteMapping("/admin/service/{id}")
	public ResponseEntity<ResponseBody<Object>> deleteService(@PathVariable Long id) {
		serviceService.deleteService(id);
		return ResponseType.ok("successfullyDeleted");
	}

}
