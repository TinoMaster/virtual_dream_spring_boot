package com.tinomaster.virtualdream.virtualdream.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import com.tinomaster.virtualdream.virtualdream.entities.User;

@Component
public class UserMapper {

	private final Converter<List<User>, List<Long>> userListToIdListConverter = new Converter<List<User>, List<Long>>() {
		@Override
		public List<Long> convert(MappingContext<List<User>, List<Long>> context) {
			return context.getSource() == null ? null
					: context.getSource().stream().map(User::getId).collect(Collectors.toList());
		}
	};

	private final Converter<User, Long> userToIdConverter = new Converter<User, Long>() {
		@Override
		public Long convert(MappingContext<User, Long> context) {
			return context.getSource() == null ? null : context.getSource().getId();
		}
	};

	public void addMapping(ModelMapper modelMapper) {
		modelMapper.addConverter(userListToIdListConverter);
		modelMapper.addConverter(userToIdConverter);
	}

	public Converter<List<User>, List<Long>> getUserListToIdListConverter() {
		return userListToIdListConverter;
	}

	public Converter<User, Long> getUserToIdConverter() {
		return userToIdConverter;
	}
}
