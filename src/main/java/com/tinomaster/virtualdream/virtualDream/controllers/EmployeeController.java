package com.tinomaster.virtualdream.virtualDream.controllers;

import org.springframework.http.ResponseEntity;
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

	@PostMapping("/admin/employee")
	public ResponseEntity<ResponseBody<Employee>> saveEmployee(@RequestBody EmployeeDto employeeDto) {
		return ResponseType.ok("successfullySaved", employeeService.saveEmployee(employeeDto));
	}
}
