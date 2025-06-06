package com.tinomaster.virtualdream.virtualdream.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.tinomaster.virtualdream.virtualdream.dtos.MachineDto;
import com.tinomaster.virtualdream.virtualdream.entities.Machine;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MachineMapper {

	private final BusinessMapper businessMapper;

	public void addMappings(ModelMapper modelMapper) {
		modelMapper.typeMap(Machine.class, MachineDto.class).addMappings(mapper -> {
			mapper.using(businessMapper.getBusinessToIdConverter()).map(Machine::getBusiness, MachineDto::setBusiness);
		});
	}
}
