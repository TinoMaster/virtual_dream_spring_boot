package com.tinomaster.virtualdream.virtualDream.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.tinomaster.virtualdream.virtualDream.dtos.BusinessDto;
import com.tinomaster.virtualdream.virtualDream.entities.Business;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BusinessMapper {

	private final UserMapper userMapper;

	public void addMappings(ModelMapper modelMapper) {
		modelMapper.typeMap(Business.class, BusinessDto.class).addMappings(mapper -> {
			mapper.using(userMapper.getUserListToIdListConverter()).map(Business::getUsers, BusinessDto::setUsers);
			mapper.using(userMapper.getUserToIdConverter()).map(Business::getOwner, BusinessDto::setOwner);
		});
	}
}
