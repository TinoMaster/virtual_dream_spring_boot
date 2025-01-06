package com.tinomaster.virtualdream.virtualDream.services;

import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualDream.entities.ServiceSale;
import com.tinomaster.virtualdream.virtualDream.repositories.ServiceSaleRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ServiceSaleService {
	private final ServiceSaleRepository serviceSaleRepository;

	public ServiceSale findOrThrow(Long id) {
		return serviceSaleRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("no se encuentra el service sale con id: " + id));
	}

}
