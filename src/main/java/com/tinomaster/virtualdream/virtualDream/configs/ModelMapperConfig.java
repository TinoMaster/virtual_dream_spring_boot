package com.tinomaster.virtualdream.virtualDream.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tinomaster.virtualdream.virtualDream.mappers.BusinessMapper;
import com.tinomaster.virtualdream.virtualDream.mappers.ServiceMapper;
import com.tinomaster.virtualdream.virtualDream.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

	private final UserMapper userMapper;
	private final BusinessMapper businessMapper;
	private final ServiceMapper serviceMapper;

	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		userMapper.addMapping(modelMapper);
		businessMapper.addMappings(modelMapper);
		serviceMapper.addMappings(modelMapper);

		return modelMapper;
	}
}
