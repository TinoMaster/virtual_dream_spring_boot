package com.tinomaster.virtualdream.virtualdream.controllers;

import com.tinomaster.virtualdream.virtualdream.dtos.BusinessFinalSaleDto;
import com.tinomaster.virtualdream.virtualdream.dtos.requests.BusinessFinalSaleByBusinessAndDateDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.BooleanResponse;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualdream.entities.BusinessFinalSale;
import com.tinomaster.virtualdream.virtualdream.mappers.BusinessFinalSaleMapper;
import com.tinomaster.virtualdream.virtualdream.services.BusinessFinalSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class BusinessFinalSaleController {

    private final BusinessFinalSaleService businessFinalSaleService;
    private final BusinessFinalSaleMapper businessFinalSaleMapper;

    private BusinessFinalSaleDto entityToDto(BusinessFinalSale businessFinalSale) {
        return businessFinalSaleMapper.entityToDto(businessFinalSale);
    }

    @PostMapping("/private/business-final-sale/getByBusinessAndDate")
    public ResponseEntity<ResponseBody<List<BusinessFinalSaleDto>>> getBusinessFinalSaleByBusinessAndDate(
            @RequestBody BusinessFinalSaleByBusinessAndDateDto requestDto) {
        var businessFinalSaleList = businessFinalSaleService.getBusinessFinalSaleByBusinessAndDate(requestDto.getBusinessId(),
                        requestDto.getStartDate(), requestDto.getEndDate()).stream()
                .toList();

        List<BusinessFinalSaleDto> businessFinalSaleDtoList = businessFinalSaleList.stream().map(this::entityToDto)
                .toList();
        return ResponseType.ok("successfullyRequest", businessFinalSaleDtoList);
    }

    @GetMapping("/admin/business-final-sale/exist-employee/{employeeId}")
    public ResponseEntity<ResponseBody<BooleanResponse>> existEmployeeInAnyBusinessFinalSale(
            @PathVariable Long employeeId) {
        boolean response = businessFinalSaleService.existEmployeeInAnyBusinessFinalSale(employeeId);
        return ResponseType.ok("successfullyRequest", BooleanResponse.builder().response(response).build());
    }

    @PostMapping("/private/business-final-sale")
    public ResponseEntity<ResponseBody<BooleanResponse>> saveBusinessFinalSale(
            @RequestBody BusinessFinalSaleDto businessFinalSaleDto) {
        businessFinalSaleService.saveBusinessFinalSale(businessFinalSaleDto);
        return ResponseType.ok("successfullySaved", BooleanResponse.builder().response(true).build());
    }

    @DeleteMapping("/admin/business-final-sale/{businessFinalSaleId}")
    public ResponseEntity<ResponseBody<BooleanResponse>> deleteBusinessFinalSale(
            @PathVariable Long businessFinalSaleId) {
        businessFinalSaleService.deleteBusinessFinalSale(businessFinalSaleId);
        return ResponseType.ok("successfullyDeleted", BooleanResponse.builder().response(true).build());
    }
}
