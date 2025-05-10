package com.tinomaster.virtualdream.virtualdream.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualdream.dtos.AddressDto;
import com.tinomaster.virtualdream.virtualdream.entities.Address;
import com.tinomaster.virtualdream.virtualdream.repositories.AddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    public Address saveAddress(AddressDto addressDto) {
        Address address = addressRepository.save(modelMapper.map(addressDto, Address.class));
        return address;
    }

    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }

}
