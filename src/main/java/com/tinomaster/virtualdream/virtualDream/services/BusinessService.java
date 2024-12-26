package com.tinomaster.virtualdream.virtualDream.services;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualDream.dtos.BusinessDto;
import com.tinomaster.virtualdream.virtualDream.entities.Address;
import com.tinomaster.virtualdream.virtualDream.entities.Business;
import com.tinomaster.virtualdream.virtualDream.entities.Employee;
import com.tinomaster.virtualdream.virtualDream.entities.User;
import com.tinomaster.virtualdream.virtualDream.repositories.BusinessRepository;
import com.tinomaster.virtualdream.virtualDream.repositories.EmployeeRepository;
import com.tinomaster.virtualdream.virtualDream.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BusinessService {

	private final BusinessRepository businessRepository;
	private final UserRepository userRepository;
	private final EmployeeRepository employeeRepository;

	private final AddressService addressService;

	private final ModelMapper mapper;

	public List<Business> getBusinessesByUserId(Long userId) {
		return businessRepository.findBusinessesByOwnerId(userId);
	}

	public Business getBusinessById(Long businessId) {
		return businessRepository.findById(businessId)
				.orElseThrow(() -> new RuntimeException("No se encuentra el business con id " + businessId));
	}

	public void deleteBusinessesByUserId(Long userId) {
		businessRepository.deleteBusinessesByUserId(userId);
	}

	public List<Business> getAllBusinesses() {
		return businessRepository.findAll();
	}

	@Transactional
	public Business saveBusiness(BusinessDto businessDto) {
		Address address = addressService.saveAddress(businessDto.getAddress());

		User user = userRepository.findById(businessDto.getOwner()).orElseThrow(
				() -> new RuntimeException("No se encuentra el user con el id: " + businessDto.getOwner()));

		Business business = Business.builder().address(address).owner(user).name(businessDto.getName())
				.phone(businessDto.getPhone()).description(businessDto.getDescription()).createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now()).build();

		return businessRepository.save(business);
	}

	@Transactional
	public void deleteBusiness(Long businessId) {
		Business business = businessRepository.findById(businessId)
				.orElseThrow(() -> new RuntimeException("No se ha encontrado el negocio con id: " + businessId));

		Long ownerId = mapper.map(business, BusinessDto.class).getOwner();

		// Obtener la lista de negocios del mismo propietario, excluyendo el negocio
		// actual
		List<Business> businessListByOwnerId = businessRepository.findBusinessesByOwnerId(ownerId).stream()
				.filter(bus -> !bus.getId().equals(businessId)) // Comprobar con equals para objetos Long
				.toList();

		List<User> employessInTheBusiness = business.getUsers();

		// Verificar si los empleados están asociados a otros negocios del propietario
		for (User user : employessInTheBusiness) {
			boolean isAssociatedWithOtherBusinesses = businessListByOwnerId.stream()
					.anyMatch(otherBusiness -> otherBusiness.getUsers().contains(user));

			// Si no está asociado a otros negocios, eliminar al usuario
			if (!isAssociatedWithOtherBusinesses) {
				Employee employee = employeeRepository.findByUserId(user.getId());
				employeeRepository.delete(employee);
			}
		}

		businessRepository.deleteById(businessId);
	}
}
