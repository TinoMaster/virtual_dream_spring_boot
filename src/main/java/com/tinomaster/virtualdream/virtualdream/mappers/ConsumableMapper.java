package com.tinomaster.virtualdream.virtualdream.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.tinomaster.virtualdream.virtualdream.dtos.ConsumableDto;
import com.tinomaster.virtualdream.virtualdream.entities.Consumable;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConsumableMapper {

	private final BusinessMapper businessMapper;

	public void addMappings(ModelMapper modelMapper) {
		modelMapper.typeMap(Consumable.class, ConsumableDto.class).addMappings(mapper -> {
			mapper.using(businessMapper.getBusinessToIdConverter()).map(Consumable::getBusiness,
					ConsumableDto::setBusiness);
		});

		modelMapper.typeMap(ConsumableDto.class, Consumable.class).addMappings(mapper -> {
			mapper.using(businessMapper.getIdToBusinessConverter()).map(ConsumableDto::getBusiness,
					Consumable::setBusiness);
		});
	}
}
