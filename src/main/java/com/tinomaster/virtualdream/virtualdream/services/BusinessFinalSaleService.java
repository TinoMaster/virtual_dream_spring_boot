package com.tinomaster.virtualdream.virtualdream.services;

import com.tinomaster.virtualdream.virtualdream.dtos.BusinessFinalSaleDto;
import com.tinomaster.virtualdream.virtualdream.entities.BusinessFinalSale;
import com.tinomaster.virtualdream.virtualdream.entities.Debt;
import com.tinomaster.virtualdream.virtualdream.entities.ServiceSale;
import com.tinomaster.virtualdream.virtualdream.mappers.BusinessFinalSaleMapper;
import com.tinomaster.virtualdream.virtualdream.repositories.BusinessFinalSaleRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.DebtRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.ServiceSaleRepository;
import com.tinomaster.virtualdream.virtualdream.utils.Log;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BusinessFinalSaleService {

    private final BusinessFinalSaleRepository businessFinalSaleRepository;
    private final DebtRepository debtRepository;
    private final ServiceSaleRepository serviceSaleRepository;
    private final BusinessFinalSaleMapper businessFinalSaleMapper;

    private BusinessFinalSale dtoToEntity(BusinessFinalSaleDto businessFinalSaleDto) {
        return businessFinalSaleMapper.dtoToEntity(businessFinalSaleDto);
    }

    public List<BusinessFinalSale> getBusinessFinalSaleByBusinessAndDate(Long businessId, LocalDateTime startDate,
                                                                         LocalDateTime endDate) {
        LocalDateTime adjustStartDate = startDate.toLocalDate().atStartOfDay();
        LocalDateTime adjustEndDate = endDate.toLocalDate().atTime(LocalTime.MAX);
        return businessFinalSaleRepository.findByBusinessAndDateRange(businessId, adjustStartDate, adjustEndDate);
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

            for (ServiceSale serviceSale : savedBusinessFinalSale.getServicesSale()) {
                serviceSale.setBusinessFinalSale(savedBusinessFinalSale);
                serviceSaleRepository.save(serviceSale);
            }

        } catch (Exception e) {
            Log.info("Error al guardar venta final: " + businessFinalSaleDto);
            throw e;
        }

    }


    public boolean existEmployeeInAnyBusinessFinalSale(Long employeeId) {
        return businessFinalSaleRepository.existEmployeeByEmployeeId(employeeId);
    }
}
