package com.tinomaster.virtualdream.virtualdream.mappers;

import com.tinomaster.virtualdream.virtualdream.dtos.MachineDto;
import com.tinomaster.virtualdream.virtualdream.dtos.MachineStateDto;
import com.tinomaster.virtualdream.virtualdream.entities.BusinessFinalSale;
import com.tinomaster.virtualdream.virtualdream.entities.Machine;
import com.tinomaster.virtualdream.virtualdream.entities.MachineState;
import com.tinomaster.virtualdream.virtualdream.repositories.BusinessFinalSaleRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.MachineRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MachineStateMapper {

    private final ModelMapper mapper;
    private final BusinessFinalSaleRepository businessFinalSaleRepository;
    private final MachineRepository machineRepository; // Asegúrate de que este repositorio existe

    public MachineStateDto entityToDto(MachineState machineState) {
        if (machineState == null) {
            return null;
        }

        // Mapea la entidad Machine anidada a MachineDto
        MachineDto machineDto = mapper.map(machineState.getMachine(), MachineDto.class);

        return MachineStateDto.builder()
                .id(machineState.getId())
                // Obtiene el ID de la entidad relacionada
                .BusinessFinalSaleId(machineState.getBusinessFinalSale() != null ? machineState.getBusinessFinalSale().getId() : null)
                .machine(machineDto) // Usa el DTO de máquina mapeado
                .fund(machineState.getFund())
                .date(machineState.getDate())
                .build();
    }

    public MachineState dtoToEntity(MachineStateDto machineStateDto) {
        if (machineStateDto == null) {
            return null;
        }

        // Busca las entidades relacionadas usando los repositorios
        BusinessFinalSale businessFinalSale = businessFinalSaleRepository.findById(machineStateDto.getBusinessFinalSaleId())
                .orElseThrow(() -> new RuntimeException("BusinessFinalSale not found with id: " + machineStateDto.getBusinessFinalSaleId())); // O manejo de error apropiado

        // Asumiendo que MachineDto tiene un ID para buscar la Machine
        Machine machine = machineRepository.findById(machineStateDto.getMachine().getId())
                .orElseThrow(() -> new RuntimeException("Machine not found with id: " + machineStateDto.getMachine().getId())); // O manejo de error apropiado

        // Construye la entidad MachineState
        MachineState machineState = new MachineState(); // O usa Builder si lo agregas a MachineState
        machineState.setId(machineStateDto.getId()); // Generalmente null al crear uno nuevo
        machineState.setBusinessFinalSale(businessFinalSale);
        machineState.setMachine(machine);
        machineState.setFund(machineStateDto.getFund());
        machineState.setDate(machineStateDto.getDate());

        return machineState;
    }
}
