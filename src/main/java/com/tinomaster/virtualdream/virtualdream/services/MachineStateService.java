package com.tinomaster.virtualdream.virtualdream.services;

import com.tinomaster.virtualdream.virtualdream.dtos.MachineStateDto;
import com.tinomaster.virtualdream.virtualdream.entities.MachineState;
import com.tinomaster.virtualdream.virtualdream.mappers.MachineStateMapper;
import com.tinomaster.virtualdream.virtualdream.repositories.MachineStateRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MachineStateService {

    private final MachineStateRepository machineStateRepository;
    private final MachineStateMapper machineStateMapper;

    // --- Métodos CRUD ---

    @Transactional
    public MachineStateDto createMachineState(MachineStateDto machineStateDto) {
        // El ID debe ser null para la creación
        machineStateDto.setId(null);
        MachineState machineStateEntity = machineStateMapper.dtoToEntity(machineStateDto);
        MachineState savedEntity = machineStateRepository.save(machineStateEntity);
        return machineStateMapper.entityToDto(savedEntity);
    }

    @Transactional(readOnly = true)
    public MachineStateDto getMachineStateById(Long id) {
        MachineState machineState = machineStateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MachineState not found with id: " + id));
        return machineStateMapper.entityToDto(machineState);
    }

    @Transactional
    public MachineStateDto updateMachineState(Long id, MachineStateDto machineStateDto) {
        if (!machineStateRepository.existsById(id)) {
            throw new EntityNotFoundException("MachineState not found with id: " + id);
        }
        // Asegura que el ID del DTO coincida con el ID de la URL para la actualización
        machineStateDto.setId(id);
        MachineState machineStateEntity = machineStateMapper.dtoToEntity(machineStateDto);
        MachineState updatedEntity = machineStateRepository.save(machineStateEntity);
        return machineStateMapper.entityToDto(updatedEntity);
    }

    @Transactional
    public void deleteMachineState(Long id) {
        if (!machineStateRepository.existsById(id)) {
            throw new EntityNotFoundException("MachineState not found with id: " + id);
        }
        machineStateRepository.deleteById(id);
    }

    // --- Métodos basados en las consultas del Repositorio ---

    @Transactional(readOnly = true)
    public List<MachineStateDto> getMachineStatesByFinalSaleId(Long finalSaleId) {
        List<MachineState> states = machineStateRepository.findByBusinessFinalSaleId(finalSaleId);
        return states.stream()
                .map(machineStateMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MachineStateDto> getMachineStatesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<MachineState> states = machineStateRepository.findByDateBetween(startDate, endDate);
        return states.stream()
                .map(machineStateMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MachineStateDto> getMachineStatesByFinalSaleAndMachineIds(Long finalSaleId, List<Long> machineIds) {
        List<MachineState> states = machineStateRepository.findByBusinessFinalSaleIdAndMachineIdIn(finalSaleId, machineIds);
        return states.stream()
                .map(machineStateMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MachineStateDto> getMachineStatesByMachineAndDateRange(Long machineId, LocalDateTime startDate, LocalDateTime endDate) {
        List<MachineState> states = machineStateRepository.findByMachineIdAndDateBetween(machineId, startDate, endDate);
        return states.stream()
                .map(machineStateMapper::entityToDto)
                .collect(Collectors.toList());
    }

    // Método de conveniencia para buscar por LocalDate
    @Transactional(readOnly = true)
    public List<MachineStateDto> getMachineStatesByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        List<MachineState> states = machineStateRepository.findByDate(startOfDay, endOfDay);
        return states.stream()
                .map(machineStateMapper::entityToDto)
                .collect(Collectors.toList());
    }

    // Método de conveniencia para buscar por Business ID y LocalDate
    @Transactional(readOnly = true)
    public List<MachineStateDto> getMachineStatesByBusinessAndDate(Long businessId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        List<MachineState> states = machineStateRepository.findByBusinessIdAndDate(businessId, startOfDay, endOfDay);
        return states.stream()
                .map(machineStateMapper::entityToDto)
                .collect(Collectors.toList());
    }

    // Método para obtener los últimos estados de máquina para un Business ID antes o en una fecha específica
    @Transactional(readOnly = true)
    public List<MachineStateDto> getLatestMachineStatesByBusinessBeforeDate(Long businessId, LocalDate targetDate) {
        // Convertimos LocalDate al final del día para asegurar que incluimos todo el día en la comparación <=
        LocalDateTime targetDateTime = targetDate.atTime(java.time.LocalTime.MAX);
        List<MachineState> states = machineStateRepository.findLatestStatesByBusinessIdBeforeDate(businessId, targetDateTime);
        return states.stream()
                .map(machineStateMapper::entityToDto)
                .collect(Collectors.toList());
    }

}
