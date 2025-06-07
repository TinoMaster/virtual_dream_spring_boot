package com.tinomaster.virtualdream.virtualdream.mappers;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import com.tinomaster.virtualdream.virtualdream.dtos.ServiceSaleDto;
import com.tinomaster.virtualdream.virtualdream.entities.Business;
import com.tinomaster.virtualdream.virtualdream.entities.BusinessFinalSale;
import com.tinomaster.virtualdream.virtualdream.entities.ServiceSale;
import com.tinomaster.virtualdream.virtualdream.repositories.BusinessRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ServiceSaleMapper {

    private final BusinessRepository businessRepository;

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

    private final Converter<BusinessFinalSale, Long> businessFinalSaleToIdConverter = new Converter<BusinessFinalSale, Long>() {

        @Override
        public Long convert(MappingContext<BusinessFinalSale, Long> context) {
            return context.getSource() == null ? null : context.getSource().getId();
        }
    };

    public void addMappings(ModelMapper modelMapper) {
        modelMapper.typeMap(ServiceSale.class, ServiceSaleDto.class).addMappings(mapper -> {
            mapper.using(businessToIdConverter).map(ServiceSale::getBusiness, ServiceSaleDto::setBusiness);
            mapper.using(businessFinalSaleToIdConverter).map(ServiceSale::getBusinessFinalSale,
                    ServiceSaleDto::setBusinessFinalSale);
        });

        modelMapper.typeMap(ServiceSaleDto.class, ServiceSale.class).addMappings(mapper -> {
            mapper.using(idToBusinessConverter).map(ServiceSaleDto::getBusiness, ServiceSale::setBusiness);
        });
    }

}
