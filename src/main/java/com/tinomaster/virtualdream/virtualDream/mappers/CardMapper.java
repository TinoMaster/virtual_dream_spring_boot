package com.tinomaster.virtualdream.virtualDream.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.tinomaster.virtualdream.virtualDream.dtos.CardDto;
import com.tinomaster.virtualdream.virtualDream.entities.Card;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CardMapper {

	private final BusinessMapper businessMapper;

	public void addMappings(ModelMapper modelMapper) {
		modelMapper.typeMap(Card.class, CardDto.class).addMappings(mapper -> {
			mapper.using(businessMapper.getBusinessToIdConverter()).map(Card::getBusinesses, CardDto::setBusinesses);
		});

		modelMapper.typeMap(CardDto.class, Card.class).addMappings(mapper -> {
			mapper.using(businessMapper.getIdToBusinessConverter()).map(CardDto::getBusinesses, Card::setBusinesses);
		});
	}
}
