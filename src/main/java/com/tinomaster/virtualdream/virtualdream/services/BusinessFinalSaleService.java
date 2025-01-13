package com.tinomaster.virtualdream.virtualdream.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualdream.dtos.BusinessFinalSaleDto;
import com.tinomaster.virtualdream.virtualdream.entities.BusinessFinalSale;
import com.tinomaster.virtualdream.virtualdream.entities.Debt;
import com.tinomaster.virtualdream.virtualdream.repositories.BusinessFinalSaleRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.DebtRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BusinessFinalSaleService {

    private final BusinessFinalSaleRepository businessFinalSaleRepository;
    private final DebtRepository debtRepository;
    private final ModelMapper mapper;

    public List<BusinessFinalSale> getBusinessFinalSaleByBusinessAndDate(Long businessId, LocalDateTime startDate,
                                                                         LocalDateTime endDate) {
        return businessFinalSaleRepository.findByBusinessAndDateRange(businessId, startDate, endDate);
    }

    @Transactional
    public BusinessFinalSale saveBusinessFinalSale(BusinessFinalSaleDto businessFinalSaleDto) {
        BusinessFinalSale businessFinalSale = mapper.map(businessFinalSaleDto, BusinessFinalSale.class);

        List<Debt> debts = businessFinalSaleDto.getDebts().stream().map(debtDto -> mapper.map(debtDto, Debt.class))
                .collect(Collectors.toList());

        List<Debt> savedDebts = debtRepository.saveAll(debts);

        businessFinalSale.setDebts(savedDebts);

        return businessFinalSaleRepository.save(businessFinalSale);
    }

    public boolean existEmployeeInAnyBusinessFinalSale(Long employeeId) {
        return businessFinalSaleRepository.existEmployeeByEmployeeId(employeeId);
    }
}
