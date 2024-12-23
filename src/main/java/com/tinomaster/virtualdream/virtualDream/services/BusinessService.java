package com.tinomaster.virtualdream.virtualDream.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualDream.entities.Business;
import com.tinomaster.virtualdream.virtualDream.repositories.BusinessRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BusinessService {

	private final BusinessRepository businessRepository;

	public List<Business> getBusinessesByUserId(Long userId) {
		return businessRepository.findBusinessesByUserId(userId);
	}

	public void deleteBusinessesByUserId(Long userId) {
		businessRepository.deleteBusinessesByUserId(userId);
	}
}
