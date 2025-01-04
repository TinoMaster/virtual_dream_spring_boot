package com.tinomaster.virtualdream.virtualDream.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualDream.dtos.CardDto;
import com.tinomaster.virtualdream.virtualDream.entities.Card;
import com.tinomaster.virtualdream.virtualDream.repositories.CardRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CardService {

	private final CardRepository cardRepository;
	private final ModelMapper mapper;

	public Card findOrThrow(Long id) {
		return cardRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("no se ha encontrado la card con el id: " + id));
	}

	public List<Card> getAllCards() {
		return cardRepository.findAll();
	}

	public Card saveCard(CardDto cardDto) {
		return cardRepository.save(mapper.map(cardDto, Card.class));
	}
}
