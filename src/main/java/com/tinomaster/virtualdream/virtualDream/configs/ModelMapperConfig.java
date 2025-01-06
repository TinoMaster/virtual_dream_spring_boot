package com.tinomaster.virtualdream.virtualDream.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tinomaster.virtualdream.virtualDream.mappers.BusinessMapper;
import com.tinomaster.virtualdream.virtualDream.mappers.CardMapper;
import com.tinomaster.virtualdream.virtualDream.mappers.ConsumableMapper;
import com.tinomaster.virtualdream.virtualDream.mappers.MachineMapper;
import com.tinomaster.virtualdream.virtualDream.mappers.ServiceMapper;
import com.tinomaster.virtualdream.virtualDream.mappers.ServiceSaleMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

	private final BusinessMapper businessMapper;
	private final ServiceMapper serviceMapper;
	private final MachineMapper machineMapper;
	private final CardMapper cardMapper;
	private final ConsumableMapper consumableMapper;
//	private final ConsumableCostMapper consumableCostMapper;
	private final ServiceSaleMapper serviceSaleMapper;

	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		businessMapper.addMappings(modelMapper);
		serviceMapper.addMappings(modelMapper);
		machineMapper.addMappings(modelMapper);
		cardMapper.addMappings(modelMapper);
		consumableMapper.addMappings(modelMapper);
//		consumableCostMapper.addMappings(modelMapper);
		serviceSaleMapper.addMappings(modelMapper);

		return modelMapper;
	}
}
