package com.tinomaster.virtualdream.virtualDream.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualDream.dtos.BusinessDto;
import com.tinomaster.virtualdream.virtualDream.dtos.EmployeeDto;
import com.tinomaster.virtualdream.virtualDream.entities.Address;
import com.tinomaster.virtualdream.virtualDream.entities.Business;
import com.tinomaster.virtualdream.virtualDream.entities.Employee;
import com.tinomaster.virtualdream.virtualDream.entities.User;
import com.tinomaster.virtualdream.virtualDream.enums.ERole;
import com.tinomaster.virtualdream.virtualDream.exceptions.InvalidRoleException;
import com.tinomaster.virtualdream.virtualDream.repositories.BusinessRepository;
import com.tinomaster.virtualdream.virtualDream.repositories.EmployeeRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeService {

	private final AddressService addressService;
	private final UserService userService;

	private final EmployeeRepository employeeRepository;
	private final BusinessRepository businessRepository;

	private final ModelMapper mapper;

	public List<Employee> getEmployees() {
		return employeeRepository.findAll();
	}

	public List<Employee> getEmployeesByBusinessId(Long businessId) {
		Business business = businessRepository.findById(businessId)
				.orElseThrow(() -> new RuntimeException("No ah encontrado el negocio con id" + businessId));

		List<Long> usersIds = mapper.map(business, BusinessDto.class).getUsers();

		return employeeRepository.findByUserIds(usersIds);
	}

	@Transactional
	public Employee saveEmployee(EmployeeDto employeeDto) {
		if (employeeDto.getUser().getRole() != ERole.EMPLOYEE) {
			throw new InvalidRoleException("El rol proporcionado no es válido para registrar un empleado.");
		}

		Address address = addressService.saveAddress(employeeDto.getAddress());
		User user = userService.saveUser(employeeDto.getUser());
		Employee employee = Employee.builder().phone(employeeDto.getPhone()).dni(employeeDto.getDni()).user(user)
				.address(address).build();

		return employeeRepository.save(employee);
	}
}
