package com.tinomaster.virtualdream.virtualdream.statistics.services;

import com.tinomaster.virtualdream.virtualdream.dtos.requests.BusinessFinalSaleByBusinessAndDateDto;
import com.tinomaster.virtualdream.virtualdream.repositories.BusinessFinalSaleRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.ServiceSaleRepository;
import com.tinomaster.virtualdream.virtualdream.statistics.dtos.HomeSalesResumeDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class HomeStatsServices {

    private final BusinessFinalSaleRepository businessFinalSaleRepository;
    private final ServiceSaleRepository serviceSaleRepository;

    public HomeSalesResumeDto getHomeSalesResumeByPeriod(BusinessFinalSaleByBusinessAndDateDto requestDto) {

        try {
            LocalDateTime adjustStartDate = requestDto.getStartDate().toLocalDate().atStartOfDay();
            LocalDateTime adjustEndDate = requestDto.getEndDate().toLocalDate().atTime(LocalTime.MAX);

            Float totalSales = calculateTotalSales(requestDto.getBusinessId(), adjustStartDate, adjustEndDate);
            Float totalServices = calculateTotalServices(requestDto.getBusinessId(), adjustStartDate, adjustEndDate);
            Float totalCost = calculateTotalCost(requestDto.getBusinessId(), adjustStartDate, adjustEndDate);
            Float totalSalaries = calculateTotalSalaries(requestDto.getBusinessId(), adjustStartDate, adjustEndDate);

            return HomeSalesResumeDto.builder()
                    .totalSales(totalSales)
                    .totalServices(totalServices)
                    .totalCost(totalCost)
                    .totalSalaries(totalSalaries)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al obtener el resumen de ventas: " + e.getMessage(), e);
        }
    }

    private Float calculateTotalSales(Long businessId, LocalDateTime startDate, LocalDateTime endDate) {
        return businessFinalSaleRepository.getTotalBusinessSales(businessId, startDate, endDate);
    }

    private Float calculateTotalServices(Long businessId, LocalDateTime startDate, LocalDateTime endDate) {
        return serviceSaleRepository.getTotalServiceSales(businessId, startDate, endDate);
    }

    private Float calculateTotalCost(Long businessId, LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }

    private Float calculateTotalSalaries(Long businessId, LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }

}
