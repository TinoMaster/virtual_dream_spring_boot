package com.tinomaster.virtualdream.virtualdream.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tinomaster.virtualdream.virtualdream.dtos.MachineDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualdream.entities.Machine;
import com.tinomaster.virtualdream.virtualdream.services.MachineService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MachineController {

	private final MachineService machineService;
	private final ModelMapper mapper;

	@PostMapping("/admin/machine")
	public ResponseEntity<ResponseBody<MachineDto>> saveMachine(@RequestBody MachineDto machineDto) {
		Machine machineSaved = machineService.saveMachina(machineDto);
		return ResponseType.ok("successfullySaved", mapper.map(machineSaved, MachineDto.class));
	}

	@DeleteMapping("/admin/machine/{id}")
	public ResponseEntity<ResponseBody<Object>> deleteMachine(@PathVariable Long id) {
		machineService.deleteMachine(id);
		return ResponseType.ok("successfullyDeleted");
	}

	@PutMapping("/admin/machine")
	public ResponseEntity<ResponseBody<Object>> updateMachine(@RequestBody MachineDto machineDto) {
		machineService.updateMachine(machineDto);
		return ResponseType.ok("successfullyUpdated");
	}
}
