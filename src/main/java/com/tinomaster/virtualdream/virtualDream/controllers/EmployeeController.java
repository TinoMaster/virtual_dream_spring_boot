package com.tinomaster.virtualdream.virtualDream.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/superadmin/employees")
	public ResponseEntity<ResponseBody<List<EmployeeDto>>> getEmployees() {
		var employeeList = StreamSupport.stream(employeeService.getEmployees().spliterator(), false).toList();
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
		var employeeList = StreamSupport.stream(employeeService.getEmployeesByBusinessId(id).spliterator(), false)
				.toList();
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

	@PostMapping("/admin/employees")
	public ResponseEntity<ResponseBody<Employee>> saveEmployee(@RequestBody EmployeeDto employeeDto) {
		return ResponseType.ok("successfullySaved", employeeService.saveEmployee(employeeDto));
	}

	@DeleteMapping("/admin/employees/{id}")
	public ResponseEntity<ResponseBody<Object>> deleteEmployee(@PathVariable Long id) {
		employeeService.deleteEmployee(id);
		return ResponseType.ok("successfullyDeleted");
	}
}
