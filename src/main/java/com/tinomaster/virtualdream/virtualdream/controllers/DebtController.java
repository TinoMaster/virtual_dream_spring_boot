package com.tinomaster.virtualdream.virtualdream.controllers;

import com.tinomaster.virtualdream.virtualdream.dtos.DebtDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualdream.entities.Debt;
import com.tinomaster.virtualdream.virtualdream.services.DebtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseBody<List<DebtDto>>> getAllDebtsByBusinessId(@PathVariable Long businessId) {
        try {
            List<DebtDto> debts = debtService.getDebtsByBusinessId(businessId).stream().map(this::mapToDebtDto).toList();
            return ResponseType.ok("successfullyRetrieved", debts);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las deudas del negocio con id: " + businessId, e);
        }
    }

    @GetMapping("/private/pending-debts/{businessId}")
    public ResponseEntity<ResponseBody<List<DebtDto>>> getAllPendingDebtsByBusinessId(@PathVariable Long businessId) {
        try {
            List<DebtDto> pendingDebts = debtService.getPendingDebtsByBusinessId(businessId).stream().map(this::mapToDebtDto).toList();
            return ResponseType.ok("successfullyRetrieved", pendingDebts);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las deudas pendientes del negocio con id: " + businessId, e);
        }
    }

    @GetMapping("/private/total-unpaid-debt/{businessId}/{startDate}/{endDate}")
    public ResponseEntity<ResponseBody<Float>> getTotalUnpaidDebtByBusinessAndDateRange(@PathVariable Long businessId, @PathVariable String startDate, @PathVariable String endDate) {
        try {
            Float totalUnpaidDebt = debtService.getTotalUnpaidDebtsByBusinessId(businessId);
            return ResponseType.ok("successfullyRetrieved", totalUnpaidDebt);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el total no pagado de deudas del negocio con id: " + businessId, e);
        }
    }

    @PostMapping("/private/debt")
    public ResponseEntity<ResponseBody<DebtDto>> createDebt(@RequestBody DebtDto debt) {
        try {
            DebtDto debtDto = mapToDebtDto(debtService.createDebt(debt));
            return ResponseType.ok("successfullyCreated", debtDto);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la deuda", e);
        }
    }

    @PutMapping("/private/debt")
    public ResponseEntity<ResponseBody<DebtDto>> updateDebt(@RequestBody DebtDto debt) {
        try {
            DebtDto debtDto = mapToDebtDto(debtService.updateDebt(debt));
            return ResponseType.ok("successfullyUpdated", debtDto);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la deuda", e);
        }
    }

    @DeleteMapping("/private/debt/{id}")
    public ResponseEntity<ResponseBody<Void>> deleteDebt(@PathVariable Long id) {
        try {
            debtService.deleteDebt(id);
            return ResponseType.ok("successfullyDeleted", null);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la deuda con id: " + id, e);
        }
    }
}
