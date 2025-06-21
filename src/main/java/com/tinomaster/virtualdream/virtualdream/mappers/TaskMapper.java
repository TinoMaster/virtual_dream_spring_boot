package com.tinomaster.virtualdream.virtualdream.mappers;

import com.tinomaster.virtualdream.virtualdream.dtos.TaskDto;
import com.tinomaster.virtualdream.virtualdream.entities.Business;
import com.tinomaster.virtualdream.virtualdream.entities.Employee;
import com.tinomaster.virtualdream.virtualdream.entities.Task;
import com.tinomaster.virtualdream.virtualdream.repositories.BusinessRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.EmployeeRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.TaskRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final BusinessRepository businessRepository;
    private final EmployeeRepository employeeRepository;

    private final Converter<Business, Long> businessToIdConverter = new Converter<Business, Long>() {
        @Override
        public Long convert(MappingContext<Business, Long> context) {
            return context.getSource() == null ? null : context.getSource().getId();
        }
    };

    private final Converter<Long, Business> idToBusinessConverter = new Converter<Long, Business>() {
        @Override
        public Business convert(MappingContext<Long, Business> context) {
            if (context.getSource() == null) return null;
            return businessRepository.findById(context.getSource()).orElse(null);
        }
    };

    private final Converter<Employee, Long> employeeToIdConverter = new Converter<Employee, Long>() {
        @Override
        public Long convert(MappingContext<Employee, Long> context) {
            return context.getSource() == null ? null : context.getSource().getId();
        }
    };

    private final Converter<Long, Employee> idToEmployeeConverter = new Converter<Long, Employee>() {
        @Override
        public Employee convert(MappingContext<Long, Employee> context) {
            if (context.getSource() == null) return null;
            return employeeRepository.findById(context.getSource()).orElse(null);
        }
    };

    public void addMappings(ModelMapper modelMapper) {
        modelMapper.typeMap(Task.class, TaskDto.class).addMappings(mapper -> {
            mapper.using(businessToIdConverter).map(Task::getBusiness, TaskDto::setBusinessId);
            mapper.using(employeeToIdConverter).map(Task::getAssignedTo, TaskDto::setAssignedToId);
        });
        modelMapper.typeMap(TaskDto.class, Task.class).addMappings(mapper -> {
            mapper.using(idToBusinessConverter).map(TaskDto::getBusinessId, Task::setBusiness);
            mapper.using(idToEmployeeConverter).map(TaskDto::getAssignedToId, Task::setAssignedTo);
        });
    }
}
