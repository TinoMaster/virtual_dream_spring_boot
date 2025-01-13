package com.tinomaster.virtualdream.virtualDream.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualDream.dtos.ConsumableDto;
import com.tinomaster.virtualdream.virtualDream.entities.Consumable;
import com.tinomaster.virtualdream.virtualDream.repositories.ConsumableRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConsumableService {

	private final ConsumableRepository consumableRepository;
	private final ModelMapper mapper;

	public Consumable findOrThrow(Long id) {
		return consumableRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("no se encuentra el consumable con id: " + id));
	}

	public List<Consumable> getConsumablesByBusinessId(Long businessId) {
		return consumableRepository.findByBusinessId(businessId);
	}

	public Consumable saveConsumable(ConsumableDto consumableDto) {
		return consumableRepository.save(mapper.map(consumableDto, Consumable.class));
	}

	public void deleteConsumable(Long id) {
		consumableRepository.deleteById(id);
	}
}
