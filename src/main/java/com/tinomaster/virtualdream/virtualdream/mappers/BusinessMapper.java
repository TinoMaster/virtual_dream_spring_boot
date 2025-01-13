package com.tinomaster.virtualdream.virtualdream.mappers;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import com.tinomaster.virtualdream.virtualdream.dtos.BusinessDto;
import com.tinomaster.virtualdream.virtualdream.entities.Business;
import com.tinomaster.virtualdream.virtualdream.repositories.BusinessRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BusinessMapper {

	private final UserMapper userMapper;
	private final BusinessRepository businessRepository;

	private final Converter<Business, Long> businessToIdConverter = new Converter<Business, Long>() {
		@Override
		public Long convert(MappingContext<Business, Long> context) {
			return context.getSource() == null ? null : context.getSource().getId();
		}
	};

	private final Converter<Long, Business> idToBusinessConverter = new Converter<Long, Business>() {
		@Override
		public Business convert(MappingContext<Long, Business> context) {
			if (context.getSource() == null) {
				return null;
			}
			return businessRepository.findById(context.getSource()).orElseThrow(
					() -> new IllegalArgumentException("Business not found with ID: " + context.getSource()));
		}
	};

	public void addMappings(ModelMapper modelMapper) {
		modelMapper.typeMap(Business.class, BusinessDto.class).addMappings(mapper -> {
			mapper.using(userMapper.getUserListToIdListConverter()).map(Business::getUsers, BusinessDto::setUsers);
			mapper.using(userMapper.getUserToIdConverter()).map(Business::getOwner, BusinessDto::setOwner);
		});
		modelMapper.typeMap(Long.class, Business.class).setConverter(idToBusinessConverter);
	}

	public Converter<Business, Long> getBusinessToIdConverter() {
		return businessToIdConverter;
	}

	public Converter<Long, Business> getIdToBusinessConverter() {
		return idToBusinessConverter;
	}
}
