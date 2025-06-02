package com.tinomaster.virtualdream.virtualdream.controllers;

import com.tinomaster.virtualdream.virtualdream.dtos.DebtDto;
import com.tinomaster.virtualdream.virtualdream.entities.Debt;
import com.tinomaster.virtualdream.virtualdream.services.DebtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DebtController {

    private final DebtService debtService;
    private final ModelMapper modelMapper;

    private DebtDto mapToDebtDto(Debt debt) {
        return modelMapper.map(debt, DebtDto.class);
    }

    @GetMapping("/private/debts/{businessId}")
    public List<DebtDto> getAllDebtsByBusinessId(@PathVariable Long businessId) {
        try {
            return debtService.getDebtsByBusinessId(businessId).stream().map(this::mapToDebtDto).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las deudas del negocio con id: " + businessId, e);
        }
    }

    @GetMapping("/private/pending-debts/{businessId}")
    public List<DebtDto> getAllPendingDebtsByBusinessId(@PathVariable Long businessId) {
        try {
            return debtService.getPendingDebtsByBusinessId(businessId).stream().map(this::mapToDebtDto).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las deudas pendientes del negocio con id: " + businessId, e);
        }
    }

    @GetMapping("/private/total-unpaid-debt/{businessId}/{startDate}/{endDate}")
    public Float getTotalUnpaidDebtByBusinessAndDateRange(@PathVariable Long businessId, @PathVariable String startDate, @PathVariable String endDate) {
        try {
            return debtService.getTotalUnpaidDebtsByBusinessId(businessId);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el total no pagado de deudas del negocio con id: " + businessId, e);
        }
    }

    @PostMapping("/private/debt")
    public DebtDto createDebt(@RequestBody DebtDto debt) {
        try {
            return mapToDebtDto(debtService.createDebt(debt));
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la deuda", e);
        }
    }

    @PutMapping("/private/debt")
    public DebtDto updateDebt(@RequestBody DebtDto debt) {
        try {
            return mapToDebtDto(debtService.updateDebt(debt));
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la deuda", e);
        }
    }
}
