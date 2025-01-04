package com.tinomaster.virtualdream.virtualDream.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tinomaster.virtualdream.virtualDream.dtos.ConsumableDto;
import com.tinomaster.virtualdream.virtualDream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualDream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualDream.entities.Consumable;
import com.tinomaster.virtualdream.virtualDream.services.ConsumableService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ConsumableController {

	private final ConsumableService consumableService;
	private final ModelMapper mapper;

	@PostMapping("/admin/consumable")
	public ResponseEntity<ResponseBody<ConsumableDto>> saveConsumable(@RequestBody ConsumableDto consumableDto) {
		Consumable consumableSaved = consumableService.saveConsumable(consumableDto);
		return ResponseType.ok("successfullySaved", mapper.map(consumableSaved, ConsumableDto.class));
	}
}
