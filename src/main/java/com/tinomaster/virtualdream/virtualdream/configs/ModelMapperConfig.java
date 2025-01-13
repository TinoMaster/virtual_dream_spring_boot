package com.tinomaster.virtualdream.virtualdream.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tinomaster.virtualdream.virtualdream.mappers.BusinessMapper;
import com.tinomaster.virtualdream.virtualdream.mappers.CardMapper;
import com.tinomaster.virtualdream.virtualdream.mappers.ConsumableMapper;
import com.tinomaster.virtualdream.virtualdream.mappers.MachineMapper;
import com.tinomaster.virtualdream.virtualdream.mappers.ServiceMapper;
import com.tinomaster.virtualdream.virtualdream.mappers.ServiceSaleMapper;

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
