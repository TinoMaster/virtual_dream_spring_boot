package com.tinomaster.virtualdream.virtualdream.configs;

import com.tinomaster.virtualdream.virtualdream.mappers.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

    private final BusinessMapper businessMapper;
    private final ServiceMapper serviceMapper;
    private final MachineMapper machineMapper;
    private final CardMapper cardMapper;
    private final ConsumableMapper consumableMapper;
    private final ServiceSaleMapper serviceSaleMapper;
    private final DebtsMapper debtsMapper;
    private final EmployeeMapper employeeMapper;
    private final TaskMapper taskMapper;

    @Bean
    ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        businessMapper.addMappings(modelMapper);
        serviceMapper.addMappings(modelMapper);
        machineMapper.addMappings(modelMapper);
        cardMapper.addMappings(modelMapper);
        consumableMapper.addMappings(modelMapper);
        serviceSaleMapper.addMappings(modelMapper);
        debtsMapper.addMappings(modelMapper);
        employeeMapper.addMappings(modelMapper);
        taskMapper.addMappings(modelMapper);

        return modelMapper;
    }
}
