package com.tinomaster.virtualdream.virtualDream.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualDream.dtos.ServiceDto;
import com.tinomaster.virtualdream.virtualDream.entities.ServiceEntity;
import com.tinomaster.virtualdream.virtualDream.repositories.ServiceRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ServiceService {

	private final ServiceRepository serviceRepository;
	private final ModelMapper mapper;

	public ServiceEntity findOrThrow(Long id) {
		return serviceRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("no se encuentra el servicio con el id: " + id));
	}

	public ServiceEntity saveService(ServiceDto serviceDto) {
		return serviceRepository.save(mapper.map(serviceDto, ServiceEntity.class));
	}

	public List<ServiceEntity> getAllServiceByBusinessId(Long businessId) {
		return serviceRepository.findByBusinessId(businessId);
	}

	public void deleteService(Long id) {
		ServiceEntity service = this.findOrThrow(id);
		serviceRepository.delete(service);
	}
}
