package com.tinomaster.virtualdream.virtualDream.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualDream.dtos.AddressDto;
import com.tinomaster.virtualdream.virtualDream.entities.Address;
import com.tinomaster.virtualdream.virtualDream.repositories.AddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {

	private final AddressRepository addressRepository;
	private final ModelMapper modelMapper;

	public Address saveAddress(AddressDto addressDto) {
		return addressRepository.save(modelMapper.map(addressDto, Address.class));
	}
}
