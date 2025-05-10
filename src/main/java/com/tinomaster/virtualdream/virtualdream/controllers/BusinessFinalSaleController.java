package com.tinomaster.virtualdream.virtualdream.controllers;

import com.tinomaster.virtualdream.virtualdream.dtos.BusinessFinalSaleDto;
import com.tinomaster.virtualdream.virtualdream.dtos.requests.BusinessFinalSaleByBusinessAndDateDto;
import com.tinomaster.virtualdream.virtualdream.dtos.requests.MonthYearRequestDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.BooleanResponse;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualdream.entities.BusinessFinalSale;
import com.tinomaster.virtualdream.virtualdream.mappers.BusinessFinalSaleMapper;
import com.tinomaster.virtualdream.virtualdream.services.BusinessFinalSaleService;
import com.tinomaster.virtualdream.virtualdream.utils.Log;
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

    @PostMapping("/private/business-final-sale/bymonth/{businessId}")
    public ResponseEntity<ResponseBody<List<BusinessFinalSaleDto>>> getBusinessFinalSalesByMonth(
            @PathVariable Long businessId,
            @RequestBody MonthYearRequestDto requestDto) {

        int year = requestDto.getYear();
        int month = requestDto.getMonth();

        if (businessId == null || businessId <= 0) {
            return ResponseType.badRequest("El ID del negocio no es válido", null);
        }

        try {
            List<BusinessFinalSale> sales = businessFinalSaleService.getBusinessFinalSalesByMonth(businessId, year, month);

            if (sales == null || sales.isEmpty()) {
                return ResponseType.notFound("No se encontraron ventas para el mes especificado.", null);
            }

            List<BusinessFinalSaleDto> salesDto = sales.stream()
                    .map(this::entityToDto) // Reutiliza el método existente
                    .toList();
            return ResponseType.ok("Ventas obtenidas correctamente para el mes: " + month + "/" + year, salesDto);

        } catch (IllegalArgumentException e) {
            // Captura el error si el mes es inválido desde el servicio
            return ResponseType.badRequest(e.getMessage(), null);
        } catch (Exception e) {
            return ResponseType.internalServerError("Error inesperado al obtener las ventas por mes: " + e.getMessage(), null);
        }
    }

    @GetMapping("/private/business-final-sale/latest/{businessId}")
    public ResponseEntity<ResponseBody<List<BusinessFinalSaleDto>>> getLatestBusinessFinalSalesWithAllMachines(@PathVariable Long businessId) {
        if (businessId == null || businessId <= 0) {
            return ResponseType.badRequest("El id del negocio no es valido", null);
        }

        try {
            List<BusinessFinalSale> businessesFinalSale = businessFinalSaleService.getLatestBusinessFinalSalesWithAllMachines(businessId);

            if (businessesFinalSale == null || businessesFinalSale.isEmpty()) {
                return ResponseType.notFound("No se encontró venta final", null);
            }

            List<BusinessFinalSaleDto> businessFinalSaleDto = businessesFinalSale.stream().map(businessFinalSaleMapper::entityToDto).toList();
            return ResponseType.ok("successfullyRequest", businessFinalSaleDto);
        } catch (Exception e) {
            return ResponseType.internalServerError("Error inesperado al obtener la venta final: " + e.getMessage(), null);
        }
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
        try {
            businessFinalSaleService.saveBusinessFinalSale(businessFinalSaleDto);
            return ResponseType.ok("successfullySaved", BooleanResponse.builder().response(true).build());
        } catch (Exception e) {
            return ResponseType.internalServerError("Error inesperado al guardar la venta final: " + e.getMessage(), null);
        }
    }

    @DeleteMapping("/admin/business-final-sale/{businessFinalSaleId}")
    public ResponseEntity<ResponseBody<BooleanResponse>> deleteBusinessFinalSale(
            @PathVariable Long businessFinalSaleId) {
        businessFinalSaleService.deleteBusinessFinalSale(businessFinalSaleId);
        return ResponseType.ok("successfullyDeleted", BooleanResponse.builder().response(true).build());
    }
}
