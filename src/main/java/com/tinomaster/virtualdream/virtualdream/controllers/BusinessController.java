package com.tinomaster.virtualdream.virtualdream.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "Bearer Authentication")
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

    @GetMapping("/superadmin/businesses/authRequests")
    public ResponseEntity<ResponseBody<List<BusinessDto>>> getBusinessesAuthRequest() {
        try {
            var businessList = StreamSupport.stream(businessService.findBusinessRequests().spliterator(), false).toList();
            List<BusinessDto> businesses = businessList.stream().map(this::businessToBusinessDto)
                    .collect(Collectors.toList());
            return ResponseType.ok("successfullyRequest", businesses);
        } catch (Exception e) {
            return ResponseType.internalServerError(e.getMessage(), null);
        }
    }

    @PutMapping("/superadmin/businesses/{ownerId}/accept")
    public ResponseEntity<ResponseBody<Boolean>> acceptBusinessRequest(@PathVariable Long ownerId) {
        try {
            businessService.acceptBusinessRequest(ownerId);
            return ResponseType.ok("successfullyUpdated", Boolean.TRUE);
        } catch (Exception e) {
            return ResponseType.internalServerError(e.getMessage(), null);
        }
    }

    @PutMapping("/superadmin/businesses/{ownerId}/reject")
    public ResponseEntity<ResponseBody<Boolean>> rejectBusinessRequest(@PathVariable Long ownerId) {
        try {
            businessService.rejectBusinessRequest(ownerId);
            return ResponseType.ok("successfullyUpdated", Boolean.TRUE);
        } catch (Exception e) {
            return ResponseType.internalServerError(e.getMessage(), null);
        }
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
