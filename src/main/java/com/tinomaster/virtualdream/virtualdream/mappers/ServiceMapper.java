package com.tinomaster.virtualdream.virtualdream.mappers;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import com.tinomaster.virtualdream.virtualdream.dtos.ServiceDto;
import com.tinomaster.virtualdream.virtualdream.entities.Business;
import com.tinomaster.virtualdream.virtualdream.entities.ServiceEntity;
import com.tinomaster.virtualdream.virtualdream.repositories.BusinessRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ServiceMapper {

	private final BusinessRepository businessRepository;

	private final Converter<Business, Long> businessToIdConverter = new Converter<Business, Long>() {
	    @Override
	    public Long convert(MappingContext<Business, Long> context) {
	        Business source = context.getSource();
	        return source == null ? null : source.getId();
	    }
	};

	private final Converter<Long, Business> idToBusinessConverter = new Converter<Long, Business>() {
	    @Override
	    public Business convert(MappingContext<Long, Business> context) {
	        Long source = context.getSource();
	        return source == null ? null : businessRepository.findById(source).orElseThrow(() ->
	            new IllegalArgumentException("Business not found for ID: " + source));
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
