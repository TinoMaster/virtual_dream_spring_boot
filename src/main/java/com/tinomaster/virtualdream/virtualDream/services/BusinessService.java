package com.tinomaster.virtualdream.virtualDream.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualDream.dtos.BusinessDto;
import com.tinomaster.virtualdream.virtualDream.entities.Address;
import com.tinomaster.virtualdream.virtualDream.entities.Business;
import com.tinomaster.virtualdream.virtualDream.entities.User;
import com.tinomaster.virtualdream.virtualDream.repositories.BusinessRepository;
import com.tinomaster.virtualdream.virtualDream.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BusinessService {

	private final BusinessRepository businessRepository;
	private final UserRepository userRepository;
	private final AddressService addressService;

	public List<Business> getBusinessesByUserId(Long userId) {
		return businessRepository.findBusinessesByUserId(userId);
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
}
