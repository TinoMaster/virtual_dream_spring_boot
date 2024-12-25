package com.tinomaster.virtualdream.virtualDream.configs;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tinomaster.virtualdream.virtualDream.dtos.BusinessDto;
import com.tinomaster.virtualdream.virtualDream.entities.Business;
import com.tinomaster.virtualdream.virtualDream.entities.User;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		// Crear un converter para mapear la lista de User a una lista de IDs
		Converter<List<User>, List<Long>> userToIdConverter = new Converter<List<User>, List<Long>>() {
			@Override
			public List<Long> convert(MappingContext<List<User>, List<Long>> context) {
				return context.getSource() == null ? null
						: context.getSource().stream().map(User::getId).collect(Collectors.toList());
			}
		};

		// Crear un converter para mapear un User a su ID
		Converter<User, Long> userToIdSingleConverter = new Converter<User, Long>() {
			@Override
			public Long convert(MappingContext<User, Long> context) {
				return context.getSource() == null ? null : context.getSource().getId();
			}
		};

		// Agregar el mapeo para Business -> BusinessDto
		modelMapper.typeMap(Business.class, BusinessDto.class).addMappings(mapper -> {
			// Mapear la lista de Users a una lista de IDs
			mapper.using(userToIdConverter).map(Business::getUsers, BusinessDto::setUsers);
			// Mapear el owner (User) a su ID
			mapper.using(userToIdSingleConverter).map(Business::getOwner, BusinessDto::setOwner);
		});

		return modelMapper;
	}
}
