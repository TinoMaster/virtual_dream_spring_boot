package com.tinomaster.virtualdream.virtualdream.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tinomaster.virtualdream.virtualdream.dtos.EmployeeDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualdream.entities.Employee;
import com.tinomaster.virtualdream.virtualdream.services.EmployeeService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ModelMapper mapper;

    private EmployeeDto employeeToEmployeeDto(Employee employee) {
        return mapper.map(employee, EmployeeDto.class);
    }

    @GetMapping("/superadmin/employees")
    public ResponseEntity<ResponseBody<List<EmployeeDto>>> getEmployees() {
        var employeeList = employeeService.getEmployees().stream().toList();
        List<EmployeeDto> employees = employeeList.stream().map(this::employeeToEmployeeDto)
                .collect(Collectors.toList());
        return ResponseType.ok("successfullyRequest", employees);
    }

    @GetMapping("/admin/employees/{id}")
    public ResponseEntity<ResponseBody<EmployeeDto>> getEmployeeById(@PathVariable Long id) {
        EmployeeDto employee = employeeToEmployeeDto(employeeService.getEmployeeById(id));
        return ResponseType.ok("successfullyRequest", employee);
    }

    @GetMapping("/admin/employees/byBusiness/{id}")
    public ResponseEntity<ResponseBody<List<EmployeeDto>>> getEmployeesByBusinessId(@PathVariable Long id) {
        var employeeList = employeeService.getEmployeesByBusinessId(id).stream().toList();
        List<EmployeeDto> employees = employeeList.stream().map(this::employeeToEmployeeDto)
                .collect(Collectors.toList());
        return ResponseType.ok("successfullyRequest", employees);
    }

    @GetMapping("/private/employees/byUserId/{id}")
    public ResponseEntity<ResponseBody<EmployeeDto>> getEmployeeByUserId(@PathVariable Long id) {
        System.out.println(id);
        EmployeeDto employee = mapper.map(employeeService.getEmployeeByUserId(id), EmployeeDto.class);
        return ResponseType.ok("successfullyRequest", employee);
    }

    @PutMapping("/admin/employees")
    public ResponseEntity<ResponseBody<EmployeeDto>> updateEmployee(@RequestBody EmployeeDto employeeDto) {
        EmployeeDto updatedEmployee = mapper.map(employeeService.updateEmployee(employeeDto), EmployeeDto.class);
        return ResponseType.ok("successfullyUpdated", updatedEmployee);
    }

    @PostMapping("/admin/employees")
    public ResponseEntity<ResponseBody<EmployeeDto>> saveEmployee(@RequestBody EmployeeDto employeeDto) {
        try {
            Employee employee = employeeService.saveEmployee(employeeDto);
            return ResponseType.ok("successfullySaved", mapper.map(employee, EmployeeDto.class));
        } catch (IllegalArgumentException e) {
            return ResponseType.badRequest(e.getMessage(), null);
        } catch (Exception e) {
            return ResponseType.internalServerError("Error inesperado al guardar el empleado: " + e.getMessage(), null);
        }
    }

    @DeleteMapping("/admin/employees/{id}")
    public ResponseEntity<ResponseBody<Object>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseType.ok("successfullyDeleted");
    }
}
