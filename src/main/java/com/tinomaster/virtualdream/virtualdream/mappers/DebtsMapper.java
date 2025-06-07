package com.tinomaster.virtualdream.virtualdream.mappers;

import com.tinomaster.virtualdream.virtualdream.dtos.CardDto;
import com.tinomaster.virtualdream.virtualdream.entities.Business;
import com.tinomaster.virtualdream.virtualdream.entities.BusinessFinalSale;
import com.tinomaster.virtualdream.virtualdream.entities.Card;
import com.tinomaster.virtualdream.virtualdream.repositories.BusinessFinalSaleRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DebtsMapper {

    private final BusinessFinalSaleRepository businessFinalSaleRepository;
    private final BusinessRepository businessRepository;

    private final Converter<Long, BusinessFinalSale> idToBusinessFinalSaleConverter = new Converter<Long, BusinessFinalSale>() {
        @Override
        public BusinessFinalSale convert(MappingContext<Long, BusinessFinalSale> context) {
            Long businessFinalSaleId = context.getSource();
            return businessFinalSaleId == null ? null : businessFinalSaleRepository.findById(businessFinalSaleId).orElseThrow();
        }
    };

    private final Converter<BusinessFinalSale, Long> businessFinalSaleToIdConverter = new Converter<BusinessFinalSale, Long>() {
        @Override
        public Long convert(MappingContext<BusinessFinalSale, Long> context) {
            return context.getSource() == null ? null : context.getSource().getId();
        }
    };

    private final Converter<Long, Business> idToBusinessConverter = new Converter<Long, Business>() {
        @Override
        public Business convert(MappingContext<Long, Business> context) {
            Long businessId = context.getSource();
            return businessId == null ? null : businessRepository.findById(businessId).orElseThrow();
        }
    };

    private final Converter<Business, Long> businessToIdConverter = new Converter<Business, Long>() {
        @Override
        public Long convert(MappingContext<Business, Long> context) {
            return context.getSource() == null ? null : context.getSource().getId();
        }
    };

    public void addMappings(ModelMapper modelMapper) {
        modelMapper.addConverter(idToBusinessFinalSaleConverter);
        modelMapper.addConverter(businessFinalSaleToIdConverter);
        modelMapper.addConverter(idToBusinessConverter);
        modelMapper.addConverter(businessToIdConverter);
    }
}
