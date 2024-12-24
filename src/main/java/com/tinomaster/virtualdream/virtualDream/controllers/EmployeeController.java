package com.tinomaster.virtualdream.virtualDream.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tinomaster.virtualdream.virtualDream.dtos.EmployeeDto;
import com.tinomaster.virtualdream.virtualDream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualDream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualDream.entities.Employee;
import com.tinomaster.virtualdream.virtualDream.services.EmployeeService;

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

	@GetMapping("/admin/employees")
	public ResponseEntity<ResponseBody<List<EmployeeDto>>> getEmployees() {
		var employeeList = StreamSupport.stream(employeeService.getEmployees().spliterator(), false).toList();
		List<EmployeeDto> employees = employeeList.stream().map(this::employeeToEmployeeDto)
				.collect(Collectors.toList());
		return ResponseType.ok("successfullyRequest", employees);
	}

	@PostMapping("/admin/employees")
	public ResponseEntity<ResponseBody<Employee>> saveEmployee(@RequestBody EmployeeDto employeeDto) {
		return ResponseType.ok("successfullySaved", employeeService.saveEmployee(employeeDto));
	}
}
