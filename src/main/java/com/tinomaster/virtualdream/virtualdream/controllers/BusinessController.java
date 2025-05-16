package com.tinomaster.virtualdream.virtualdream.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tinomaster.virtualdream.virtualdream.dtos.BusinessDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualdream.entities.Business;
import com.tinomaster.virtualdream.virtualdream.services.BusinessService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class BusinessController {

    private final ModelMapper mapper;
    private final BusinessService businessService;

    private BusinessDto businessToBusinessDto(Business business) {
        return mapper.map(business, BusinessDto.class);
    }

    @GetMapping("/superadmin/businesses")
    public ResponseEntity<ResponseBody<List<BusinessDto>>> getBusinesses() {
        var businessList = StreamSupport.stream(businessService.getAllBusinesses().spliterator(), false).toList();
        List<BusinessDto> businesses = businessList.stream().map(this::businessToBusinessDto)
                .collect(Collectors.toList());
        return ResponseType.ok("successfullyRequest", businesses);
    }

    @GetMapping("/owner/businesses/{id}")
    public ResponseEntity<ResponseBody<BusinessDto>> getBusinessById(@PathVariable Long id) {
        return ResponseType.ok("successfullyRequest",
                mapper.map(businessService.getBusinessById(id), BusinessDto.class));
    }

    @PostMapping("/owner/businesses")
    public ResponseEntity<ResponseBody<BusinessDto>> saveBusiness(@RequestBody BusinessDto businessDto) {
        return ResponseType.ok("successfullySaved",
                mapper.map(businessService.saveBusiness(businessDto), BusinessDto.class));
    }

    @PutMapping("/owner/businesses/{id}")
    public ResponseEntity<ResponseBody<BusinessDto>> updateBusiness(@PathVariable Long id, @RequestBody BusinessDto businessDto) {
        try {
            return ResponseType.ok("successfullyUpdated",
                    mapper.map(businessService.updateBusiness(id, businessDto), BusinessDto.class));
        } catch (Exception e) {
            return ResponseType.internalServerError(e.getMessage(), null);
        }
    }

    @DeleteMapping("/owner/businesses/{id}")
    public ResponseEntity<ResponseBody<Object>> deleteById(@PathVariable Long id) {
        businessService.deleteBusiness(id);
        return ResponseType.ok("successfullyDelete");
    }
}
