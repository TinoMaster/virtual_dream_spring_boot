package com.tinomaster.virtualdream.virtualdream.controllers;

import com.tinomaster.virtualdream.virtualdream.dtos.AddressDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualdream.services.AddressService;
import com.tinomaster.virtualdream.virtualdream.utils.Log;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@SecurityRequirement(name = "Bearer Authentication")
public class AddressController {

    private final AddressService addressService;
    private final ModelMapper mapper;

    @PostMapping("/admin/address")
    public ResponseEntity<ResponseBody<AddressDto>> saveAddress(@RequestBody AddressDto address) {
        AddressDto addressDto = mapper.map(addressService.saveAddress(address), AddressDto.class);
        return ResponseType.ok("successfullySaved", addressDto);
    }
}
