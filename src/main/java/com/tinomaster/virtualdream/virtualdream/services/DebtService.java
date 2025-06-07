package com.tinomaster.virtualdream.virtualdream.services;

import com.tinomaster.virtualdream.virtualdream.dtos.DebtDto;
import com.tinomaster.virtualdream.virtualdream.entities.Business;
import com.tinomaster.virtualdream.virtualdream.entities.Debt;
import com.tinomaster.virtualdream.virtualdream.entities.Employee;
import com.tinomaster.virtualdream.virtualdream.repositories.DebtRepository;
import com.tinomaster.virtualdream.virtualdream.services.interfaces.DebtServiceInterface;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class DebtService implements DebtServiceInterface {

    private final DebtRepository debtRepository;
    private final ModelMapper modelMapper;
    private final EmployeeService employeeService;

    @Override
    public Float getTotalUnpaidDebtsByBusinessId(Long businessId) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        try {
            return debtRepository.getTotalUnpaidDebtsByBusinessAndDateRange(businessId, startDate, endDate);
        } catch (Exception e) {
            return 0f;
        }
    }

    @Override
    public List<Debt> getDebtsByBusinessId(Long businessId) {
        try {
            return debtRepository.findDebtsByBusinessId(businessId);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las deudas del negocio con id: " + businessId, e);
        }
    }

    @Override
    public List<Debt> getPendingDebtsByBusinessId(Long businessId) {
        try {
            return debtRepository.findPendingDebtsByBusinessId(businessId);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las deudas pendientes del negocio con id: " + businessId, e);
        }
    }

    @Override
    public Debt createDebt(DebtDto debt) {
        try {
            Business business = modelMapper.map(debt.getBusiness(), Business.class);
            Employee employee = employeeService.findOrThrow(debt.getEmployee().getId());
            Debt newDebt = Debt.builder()
                    .business(business)
                    .employee(employee)
                    .total(debt.getTotal())
                    .name(debt.getName())
                    .description(debt.getDescription())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .paid(debt.getPaid())
                    .build();
            return debtRepository.save(newDebt);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la deuda", e);
        }
    }

    @Override
    public Debt updateDebt(DebtDto debt) {
        try {
            Debt updatedDebt = modelMapper.map(debt, Debt.class);
            return debtRepository.save(updatedDebt);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la deuda", e);
        }
    }

    @Override
    public void deleteDebt(Long id) {
        try {
            debtRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la deuda con id: " + id, e);
        }
    }
}
