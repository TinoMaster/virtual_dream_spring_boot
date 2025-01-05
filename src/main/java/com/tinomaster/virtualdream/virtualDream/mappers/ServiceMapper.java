package com.tinomaster.virtualdream.virtualDream.mappers;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import com.tinomaster.virtualdream.virtualDream.dtos.ServiceDto;
import com.tinomaster.virtualdream.virtualDream.entities.Business;
import com.tinomaster.virtualdream.virtualDream.entities.ServiceEntity;
import com.tinomaster.virtualdream.virtualDream.repositories.BusinessRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ServiceMapper {

	private final BusinessRepository businessRepository;

	private final Converter<ServiceEntity, Long> businessToIdConverter = new Converter<ServiceEntity, Long>() {
		@Override
		public Long convert(MappingContext<ServiceEntity, Long> context) {
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
		modelMapper.typeMap(ServiceEntity.class, ServiceDto.class).addMappings(mapper -> {
			mapper.using(businessToIdConverter).map(ServiceEntity::getBusiness, ServiceDto::setBusiness);
		});

		modelMapper.typeMap(ServiceDto.class, ServiceEntity.class).addMappings(mapper -> {
			mapper.using(idToBusinessConverter).map(ServiceDto::getBusiness, ServiceEntity::setBusiness);
		});
	}
}
