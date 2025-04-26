package com.tinomaster.virtualdream.virtualdream.mappers;

import com.tinomaster.virtualdream.virtualdream.dtos.*;
import com.tinomaster.virtualdream.virtualdream.entities.*;
import com.tinomaster.virtualdream.virtualdream.repositories.BusinessRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BusinessFinalSaleMapper {

    private final ModelMapper mapper;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;

    public BusinessFinalSaleDto entityToDto(BusinessFinalSale businessFinalSale) {
        return BusinessFinalSaleDto.builder()
                .id(businessFinalSale.getId())
                .name(businessFinalSale.getName())
                .business(businessFinalSale.getBusiness().getId())
                .total(businessFinalSale.getTotal())
                .paid(businessFinalSale.getPaid())
                .debts(businessFinalSale.getDebts().stream().map(debt -> mapper.map(debt, DebtDto.class)).toList())
                .machines(businessFinalSale.getMachines().stream().map(machine -> mapper.map(machine, MachineDto.class)).toList())
                .note(businessFinalSale.getNote())
                .workers(businessFinalSale.getWorkers().stream().map(employee -> mapper.map(employee, EmployeeDto.class)).toList())
                .servicesSales(businessFinalSale.getServicesSale().stream().map(serviceSale -> mapper.map(serviceSale, ServiceSaleDto.class)).toList())
                .doneBy(businessFinalSale.getDoneBy().getId())
                .fund(businessFinalSale.getFund())
                .cards(businessFinalSale.getCards().stream().map(card -> mapper.map(card, CardDto.class)).toList())
                .createdAt(businessFinalSale.getCreatedAt())
                .updatedAt(businessFinalSale.getUpdatedAt())
                .build();
    }

    public BusinessFinalSale dtoToEntity(BusinessFinalSaleDto businessFinalSaleDto) {
        Business business = businessRepository.findById(businessFinalSaleDto.getBusiness()).orElseThrow();
        User user = userRepository.findById(businessFinalSaleDto.getDoneBy()).orElseThrow();

        return BusinessFinalSale.builder()
                .id(businessFinalSaleDto.getId())
                .name(businessFinalSaleDto.getName())
                .business(business)
                .total(businessFinalSaleDto.getTotal())
                .paid(businessFinalSaleDto.getPaid())
                .debts(businessFinalSaleDto.getDebts().stream().map(debtDto -> mapper.map(debtDto, Debt.class)).toList())
                .machines(businessFinalSaleDto.getMachines().stream().map(machineDto -> mapper.map(machineDto, Machine.class)).toList())
                .note(businessFinalSaleDto.getNote())
                .workers(businessFinalSaleDto.getWorkers().stream().map(employeeDto -> mapper.map(employeeDto, Employee.class)).toList())
                .servicesSale(businessFinalSaleDto.getServicesSales().stream().map(serviceSaleDto -> mapper.map(serviceSaleDto, ServiceSale.class)).toList())
                .doneBy(user)
                .fund(businessFinalSaleDto.getFund())
                .cards(businessFinalSaleDto.getCards().stream().map(cardDto -> mapper.map(cardDto, Card.class)).toList())
                .createdAt(businessFinalSaleDto.getCreatedAt())
                .updatedAt(businessFinalSaleDto.getUpdatedAt())
                .build();
    }
}
