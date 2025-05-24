package com.tinomaster.virtualdream.virtualdream.statistics.controllers;

import com.tinomaster.virtualdream.virtualdream.dtos.requests.BusinessFinalSaleByBusinessAndDateDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualdream.statistics.dtos.HomeSalesResumeDto;
import com.tinomaster.virtualdream.virtualdream.statistics.services.HomeStatsServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class HomeStatsController {

    private final HomeStatsServices homeStatsServices;

    @PostMapping("/private/home-stats/sales-resume-by-period")
    public ResponseEntity<ResponseBody<HomeSalesResumeDto>> getHomeSalesResumeByPeriod(@RequestBody BusinessFinalSaleByBusinessAndDateDto requestDto) {
        try {
            if (requestDto.getBusinessId() == null || requestDto.getBusinessId() <= 0) {
                return ResponseType.badRequest("El ID del negocio no es valido", null);
            }
            return ResponseType.ok("successfullyRequest", homeStatsServices.getHomeSalesResumeByPeriod(requestDto));
        } catch (Exception e) {
            return ResponseType.internalServerError("Error inesperado al obtener el resumen de ventas: " + e.getMessage(), null);
        }
    }
}
