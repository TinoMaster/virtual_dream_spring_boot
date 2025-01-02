package com.tinomaster.virtualdream.virtualDream.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualDream.dtos.MachineDto;
import com.tinomaster.virtualdream.virtualDream.entities.Business;
import com.tinomaster.virtualdream.virtualDream.entities.Machine;
import com.tinomaster.virtualdream.virtualDream.repositories.BusinessRepository;
import com.tinomaster.virtualdream.virtualDream.repositories.MachineRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MachineService {

	private final MachineRepository machineRepository;
	private final BusinessRepository businessRepository;

	private final ModelMapper mapper;

	public Machine findOrThrow(Long id) {
		return machineRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("la maquina con id: " + id + " no se encuentra"));
	}

	@Transactional
	public Machine saveMachina(MachineDto machineDto) {

		Machine machineToSave = mapper.map(machineDto, Machine.class);

		Business business = businessRepository.findById(machineDto.getBusiness())
				.orElseThrow(() -> new RuntimeException("Business not found with ID: " + machineDto.getBusiness()));

		machineToSave.setBusiness(business);
		Machine machineSaved = machineRepository.save(machineToSave);

		business.getMachines().add(machineSaved);

		businessRepository.save(business);

		return machineSaved;
	}

	@Transactional
	public void deleteMachine(Long id) {
		Machine machine = this.findOrThrow(id);

		Business business = machine.getBusiness();

		if (business != null) {
			business.getMachines().removeIf(m -> m.getId().equals(id));

			businessRepository.save(business);
		} else {
			throw new RuntimeException("La machine no pertenece a ningun business");
		}
	}
	
	public void updateMachine(MachineDto machineDto) {
		if(machineDto.getId() == null) {
			throw new RuntimeException("falta el id de la maquina para poder actualizarla");
		}
		
		Machine machine = this.findOrThrow(machineDto.getId());
		
		machine.setActive(machineDto.isActive());
		machine.setName(machineDto.getName());
		
		machineRepository.save(machine);
	}
}
