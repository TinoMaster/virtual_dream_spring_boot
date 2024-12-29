package com.tinomaster.virtualdream.virtualDream.mappers;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import com.tinomaster.virtualdream.virtualDream.dtos.ServiceDto;
import com.tinomaster.virtualdream.virtualDream.entities.Business;
import com.tinomaster.virtualdream.virtualDream.entities.Service;
import com.tinomaster.virtualdream.virtualDream.repositories.BusinessRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ServiceMapper {

	private final BusinessRepository businessRepository;

	private final Converter<Service, Long> businessToIdConverter = new Converter<Service, Long>() {
		@Override
		public Long convert(MappingContext<Service, Long> context) {
			return context.getSource().getBusiness() == null ? null : context.getSource().getBusiness().getId();
		}
	};

	private final Converter<Long, Business> idToBusinessConverter = new Converter<Long, Business>() {
		@Override
		public Business convert(MappingContext<Long, Business> context) {
			Long businessId = context.getSource();
			return businessId == null ? null : businessRepository.findById(businessId).orElseThrow();
		}
	};

	public void addMappings(ModelMapper modelMapper) {
		modelMapper.typeMap(Service.class, ServiceDto.class).addMappings(mapper -> {
			mapper.using(businessToIdConverter).map(Service::getBusiness, ServiceDto::setBusiness);
		});

		modelMapper.typeMap(ServiceDto.class, Service.class).addMappings(mapper -> {
			mapper.using(idToBusinessConverter).map(ServiceDto::getBusiness, Service::setBusiness);
		});
	}
}
