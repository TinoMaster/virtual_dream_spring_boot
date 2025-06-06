package com.tinomaster.virtualdream.virtualdream.mappers;

import com.tinomaster.virtualdream.virtualdream.dtos.DebtDto;
import com.tinomaster.virtualdream.virtualdream.entities.BusinessFinalSale;
import com.tinomaster.virtualdream.virtualdream.entities.Debt;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardMapper {


    private final Converter<BusinessFinalSale, Long> businessFinalSaleToIdConverter = context -> context.getSource() == null ? null : context.getSource().getId();

    public void addMappings(ModelMapper modelMapper) {
        modelMapper.typeMap(Debt.class, DebtDto.class).addMappings(mapper ->
                mapper.using(businessFinalSaleToIdConverter).map(Debt::getBusinessFinalSale, DebtDto::setBusinessFinalSale)
        );
    }
}
