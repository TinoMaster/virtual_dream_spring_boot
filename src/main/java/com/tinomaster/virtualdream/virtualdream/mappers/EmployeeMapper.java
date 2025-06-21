package com.tinomaster.virtualdream.virtualdream.mappers;

import com.tinomaster.virtualdream.virtualdream.entities.Employee;
import com.tinomaster.virtualdream.virtualdream.repositories.EmployeeRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {

    private final EmployeeRepository employeeRepository;

    @Getter
    private final Converter<Employee, Long> employeeToIdConverter = new Converter<Employee, Long>() {
        @Override
        public Long convert(MappingContext<Employee, Long> context) {
            return context.getSource() == null ? null : context.getSource().getId();
        }
    };

    @Getter
    private final Converter<Long, Employee> idToEmployeeConverter = new Converter<Long, Employee>() {
        @Override
        public Employee convert(MappingContext<Long, Employee> context) {
            Long employeeId = context.getSource();
            return employeeId == null ? null : employeeRepository.findById(employeeId).orElseThrow();
        }
    };

    public void addMappings(ModelMapper modelMapper) {
        modelMapper.addConverter(employeeToIdConverter);
        modelMapper.addConverter(idToEmployeeConverter);
    }
}
