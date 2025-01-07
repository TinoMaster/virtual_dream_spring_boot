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

	public Employee findOrThrow(final Long id) {
		return employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Employee by id " + id + " not found"));
	}

	public List<Employee> getEmployees() {
		return employeeRepository.findAll();
	}

	public List<Employee> getEmployeesByBusinessId(Long businessId) {
		Business business = businessRepository.findById(businessId)
				.orElseThrow(() -> new RuntimeException("No ah encontrado el negocio con id" + businessId));

		List<Long> usersIds = mapper.map(business, BusinessDto.class).getUsers();

		return employeeRepository.findByUserIds(usersIds);
	}

	public Employee getEmployeeById(Long id) {
		return employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("No se ha encontrado un empleado con el id: " + id));
	}

	public Employee getEmployeeByUserId(Long userId) {
		Employee employee;
		try {
			employee = employeeRepository.findByUserId(userId);
		} catch (Exception e) {
			throw new RuntimeException("no se encontro el employee");
		}
		return employee;
	}

	@Transactional
	public Employee saveEmployee(EmployeeDto employeeDto) {
		ERole role = employeeDto.getUser().getRole();
		System.out.println();
		if (role != ERole.EMPLOYEE && role != ERole.ADMIN && role != ERole.USER) {
			throw new InvalidRoleException("El rol proporcionado no es válido para registrar un empleado.");
		}

		Address address = addressService.saveAddress(employeeDto.getAddress());
		User user = userService.saveUser(employeeDto.getUser());
		Employee employee = Employee.builder().phone(employeeDto.getPhone()).dni(employeeDto.getDni()).user(user)
				.address(address).build();

		return employeeRepository.save(employee);
	}

	@Transactional
	public void deleteEmployee(Long id) {
		Employee employee = this.findOrThrow(id);

		User user = employee.getUser();

		List<Business> businesses = user.getBusinesses();

		if (!businesses.isEmpty()) {
			for (Business business : businesses) {
				business.getUsers().remove(user);
				businessRepository.save(business);
			}
		}

		employeeRepository.delete(employee);

	}
}
