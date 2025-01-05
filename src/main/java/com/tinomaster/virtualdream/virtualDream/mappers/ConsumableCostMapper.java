package com.tinomaster.virtualdream.virtualDream.mappers;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import com.tinomaster.virtualdream.virtualDream.dtos.ConsumableCostDto;
import com.tinomaster.virtualdream.virtualDream.entities.ConsumableCost;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConsumableCostMapper {

	private final Converter<ConsumableCost, Long> consumableToIdConverter = new Converter<ConsumableCost, Long>() {

		@Override
		public Long convert(MappingContext<ConsumableCost, Long> context) {
			return context.getSource().getConsumable() == null ? null : context.getSource().getConsumable().getId();
		}
	};

	public void addMappings(ModelMapper modelMapper) {
		modelMapper.createTypeMap(ConsumableCost.class, ConsumableCostDto.class).addMappings(mapper -> {
			mapper.using(consumableToIdConverter).map(ConsumableCost::getConsumable, ConsumableCostDto::setConsumable);
		});
	}
}
