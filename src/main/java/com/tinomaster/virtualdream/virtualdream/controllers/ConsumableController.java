package com.tinomaster.virtualdream.virtualdream.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tinomaster.virtualdream.virtualdream.dtos.ConsumableDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualdream.entities.Consumable;
import com.tinomaster.virtualdream.virtualdream.services.ConsumableService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ConsumableController {

	private final ConsumableService consumableService;
	private final ModelMapper mapper;

	private ConsumableDto consumableToConsumableDto(Consumable consumable) {
		return mapper.map(consumable, ConsumableDto.class);
	}

	@GetMapping("/admin/consumable/list/{id}")
	public ResponseEntity<ResponseBody<List<ConsumableDto>>> getAllByBusinessId(@PathVariable Long id) {
		var consumableList = StreamSupport.stream(consumableService.getConsumablesByBusinessId(id).spliterator(), false)
				.toList();
		List<ConsumableDto> consumables = consumableList.stream().map(this::consumableToConsumableDto)
				.collect(Collectors.toList());
		return ResponseType.ok("successfullyRequest", consumables);
	}

	@PostMapping("/admin/consumable")
	public ResponseEntity<ResponseBody<ConsumableDto>> saveConsumable(@RequestBody ConsumableDto consumableDto) {
		Consumable consumableSaved = consumableService.saveConsumable(consumableDto);
		return ResponseType.ok("successfullySaved", mapper.map(consumableSaved, ConsumableDto.class));
	}

	@DeleteMapping("/admin/consumable/{id}")
	public ResponseEntity<ResponseBody<Object>> deleteConsumable(@PathVariable Long id) {
		consumableService.deleteConsumable(id);
		return ResponseType.ok("successfullyDeleted");
	}
}
