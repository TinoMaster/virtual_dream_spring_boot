package com.tinomaster.virtualdream.virtualdream.controllers;

import com.tinomaster.virtualdream.virtualdream.dtos.MachineStateDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualdream.services.MachineStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MachineStateController {

    private final MachineStateService machineStateService;

    // --- CRUD Endpoints ---

    @PostMapping("/admin/machine-state")
    public ResponseEntity<ResponseBody<MachineStateDto>> createMachineState(@RequestBody MachineStateDto machineStateDto) {
        MachineStateDto createdDto = machineStateService.createMachineState(machineStateDto);
        // Usando el ResponseType para estandarizar la respuesta
        return ResponseType.ok("successfullySaved", createdDto);
    }

    @GetMapping("/private/machine-state/{id}")
    public ResponseEntity<ResponseBody<MachineStateDto>> getMachineStateById(@PathVariable Long id) {
        MachineStateDto dto = machineStateService.getMachineStateById(id);
        return ResponseType.ok("successfullyFetched", dto);
    }

    @PutMapping("/admin/machine-state/{id}")
    public ResponseEntity<ResponseBody<MachineStateDto>> updateMachineState(@PathVariable Long id, @RequestBody MachineStateDto machineStateDto) {
        MachineStateDto updatedDto = machineStateService.updateMachineState(id, machineStateDto);
        return ResponseType.ok("successfullyUpdated", updatedDto);
    }

    @DeleteMapping("/owner/machine-state/{id}")
    public ResponseEntity<ResponseBody<Object>> deleteMachineState(@PathVariable Long id) {
        machineStateService.deleteMachineState(id);
        return ResponseType.ok("successfullyDeleted");
    }

    // --- Custom Query Endpoints ---

    @GetMapping("/private/machine-state/by-final-sale/{finalSaleId}")
    public ResponseEntity<ResponseBody<List<MachineStateDto>>> getStatesByFinalSaleId(@PathVariable Long finalSaleId) {
        List<MachineStateDto> dtos = machineStateService.getMachineStatesByFinalSaleId(finalSaleId);
        return ResponseType.ok("successfullyFetched", dtos);
    }

    @GetMapping("/private/machine-state/by-date-range")
    public ResponseEntity<ResponseBody<List<MachineStateDto>>> getStatesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<MachineStateDto> dtos = machineStateService.getMachineStatesByDateRange(startDate, endDate);
        return ResponseType.ok("successfullyFetched", dtos);
    }

    @GetMapping("/private/machine-state/by-final-sale-and-machine-ids/{finalSaleId}")
    public ResponseEntity<ResponseBody<List<MachineStateDto>>> getStatesByFinalSaleAndMachineIds(
            @PathVariable Long finalSaleId,
            @RequestParam List<Long> machineIds) {
        List<MachineStateDto> dtos = machineStateService.getMachineStatesByFinalSaleAndMachineIds(finalSaleId, machineIds);
        return ResponseType.ok("successfullyFetched", dtos);
    }

    @GetMapping("/private/machine-state/by-machine-and-date-range/{machineId}")
    public ResponseEntity<ResponseBody<List<MachineStateDto>>> getStatesByMachineAndDateRange(
            @PathVariable Long machineId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<MachineStateDto> dtos = machineStateService.getMachineStatesByMachineAndDateRange(machineId, startDate, endDate);
        return ResponseType.ok("successfullyFetched", dtos);
    }

    @GetMapping("/private/machine-state/by-date")
    public ResponseEntity<ResponseBody<List<MachineStateDto>>> getStatesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<MachineStateDto> dtos = machineStateService.getMachineStatesByDate(date);
        return ResponseType.ok("successfullyFetched", dtos);
    }

    @GetMapping("/private/machine-state/by-business-and-date/{businessId}")
    public ResponseEntity<ResponseBody<List<MachineStateDto>>> getStatesByBusinessAndDate(
            @PathVariable Long businessId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<MachineStateDto> dtos = machineStateService.getMachineStatesByBusinessAndDate(businessId, date);
        return ResponseType.ok("successfullyFetched", dtos);
    }
}
