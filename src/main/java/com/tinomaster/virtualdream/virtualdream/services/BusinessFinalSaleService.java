package com.tinomaster.virtualdream.virtualdream.services;

import com.tinomaster.virtualdream.virtualdream.dtos.BusinessFinalSaleDto;
import com.tinomaster.virtualdream.virtualdream.dtos.MachineStateDto;
import com.tinomaster.virtualdream.virtualdream.entities.*;
import com.tinomaster.virtualdream.virtualdream.mappers.BusinessFinalSaleMapper;
import com.tinomaster.virtualdream.virtualdream.repositories.BusinessFinalSaleRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.CardRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.DebtRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.ServiceSaleRepository;
import com.tinomaster.virtualdream.virtualdream.utils.Log;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@AllArgsConstructor
public class BusinessFinalSaleService {

    private final BusinessFinalSaleRepository businessFinalSaleRepository;
    private final DebtRepository debtRepository;
    private final ServiceSaleRepository serviceSaleRepository;
    private final CardRepository cardRepository;
    private final BusinessFinalSaleMapper businessFinalSaleMapper;
    private final MachineStateService machineStateService;

    private final ServiceSaleService serviceSaleService;

    private BusinessFinalSale dtoToEntity(BusinessFinalSaleDto businessFinalSaleDto) {
        return businessFinalSaleMapper.dtoToEntity(businessFinalSaleDto);
    }

    public List<BusinessFinalSale> getBusinessFinalSaleByBusinessAndDate(Long businessId, LocalDateTime startDate,
                                                                         LocalDateTime endDate) {
        LocalDateTime adjustStartDate = startDate.toLocalDate().atStartOfDay();
        LocalDateTime adjustEndDate = endDate.toLocalDate().atTime(LocalTime.MAX);
        return businessFinalSaleRepository.findByBusinessAndDateRange(businessId, adjustStartDate, adjustEndDate);
    }

    public List<BusinessFinalSale> getBusinessFinalSalesByMonth(Long businessId, int year, int month) {
        // Asegurarse de que el mes es válido (1-12)
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12.");
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        // Usamos TemporalAdjusters.lastDayOfMonth() para obtener el último día del mes
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);

        Log.info("Buscando ventas para Business ID: " + businessId + " en el mes " + month + "/" + year +
                " (Desde: " + startDate + " Hasta: " + endDate + ")");

        return businessFinalSaleRepository.findByBusinessAndDateRange(businessId, startDate, endDate);
    }

    @Transactional
    public void saveBusinessFinalSale(BusinessFinalSaleDto businessFinalSaleDto) {
        Log.info("Guardando venta final: " + businessFinalSaleDto);
        BusinessFinalSale businessFinalSale = dtoToEntity(businessFinalSaleDto);

        try {
            List<Debt> debts = businessFinalSale.getDebts();

            List<Debt> savedDebts = debtRepository.saveAll(debts);

            businessFinalSale.setDebts(savedDebts);
            Log.info("ServiceSales recibidos del DTO: " + businessFinalSale.getServicesSale());

            for (ServiceSale serviceSale : businessFinalSale.getServicesSale()) {
                if (serviceSale.getId() != null) {
                    Log.info("Eliminando ServiceSale antiguo con ID: " + serviceSale.getId());
                    ServiceSale oldServiceSale = serviceSaleRepository.findById(serviceSale.getId()).orElseThrow();
                    serviceSaleRepository.delete(oldServiceSale);
                }
                serviceSale.setId(null);
            }

            BusinessFinalSale savedBusinessFinalSale = businessFinalSaleRepository.save(businessFinalSale);

            // actualizamos las ventas de servicios
            for (ServiceSale serviceSale : savedBusinessFinalSale.getServicesSale()) {
                serviceSale.setBusinessFinalSale(savedBusinessFinalSale);
                serviceSaleRepository.save(serviceSale);
            }

            // actualizamos las cards
            for (Card card : savedBusinessFinalSale.getCards()) {
                card.setBusinessFinalSale(savedBusinessFinalSale);
                cardRepository.save(card);
            }

            // actualizamos las debts
            for (Debt debt : savedBusinessFinalSale.getDebts()) {
                debt.setBusinessFinalSale(savedBusinessFinalSale);
                debtRepository.save(debt);
            }

            // salvamos las machineState
            for (MachineStateDto machineStateDto : businessFinalSaleDto.getMachineStates()) {
                machineStateDto.setBusinessFinalSaleId(savedBusinessFinalSale.getId());
                machineStateService.createMachineState(machineStateDto);
            }

        } catch (Exception e) {
            Log.info("Error al guardar venta final: " + businessFinalSaleDto);
            throw e;
        }

    }

    @Transactional
    public void deleteBusinessFinalSale(Long businessFinalSaleId) {
        BusinessFinalSale businessFinalSale = businessFinalSaleRepository.findById(businessFinalSaleId).orElseThrow();

        List<ServiceSale> serviceSales = businessFinalSale.getServicesSale();
        List<MachineStateDto> machineStateList = machineStateService.getMachineStatesByFinalSaleId(businessFinalSaleId);
        for (ServiceSale serviceSale : serviceSales) {
            serviceSaleService.deleteServiceSale(serviceSale.getId(), true);
        }

        for (MachineStateDto machineStateDto : machineStateList) {
            machineStateService.deleteMachineState(machineStateDto.getId());
        }

        businessFinalSaleRepository.delete(businessFinalSale);
    }


    public boolean existEmployeeInAnyBusinessFinalSale(Long employeeId) {
        return businessFinalSaleRepository.existEmployeeByEmployeeId(employeeId);
    }

    public List<BusinessFinalSale> getLatestBusinessFinalSalesWithAllMachines(Long businessId) {
        return businessFinalSaleRepository.getLatestBusinessFinalSalesWithAllMachines(businessId);
    }
}
