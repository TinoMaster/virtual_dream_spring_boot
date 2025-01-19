package com.tinomaster.virtualdream.virtualdream.mappers;

import com.tinomaster.virtualdream.virtualdream.dtos.CardDto;
import com.tinomaster.virtualdream.virtualdream.entities.BusinessFinalSale;
import com.tinomaster.virtualdream.virtualdream.entities.Card;
import com.tinomaster.virtualdream.virtualdream.repositories.BusinessFinalSaleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DebtsMapper {

    private final BusinessFinalSaleRepository businessFinalSaleRepository;

    private final Converter<BusinessFinalSale, Long> businessFinalSaleToIdConverter = context -> context.getSource() == null ? null : context.getSource().getId();

    public void addMappings(ModelMapper modelMapper) {
        modelMapper.typeMap(Card.class, CardDto.class).addMappings(mapper ->
                mapper.using(businessFinalSaleToIdConverter).map(Card::getBusinessFinalSale, CardDto::setBusinessFinalSale)
        );
    }
}
