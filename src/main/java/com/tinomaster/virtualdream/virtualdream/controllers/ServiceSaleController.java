package com.tinomaster.virtualdream.virtualdream.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tinomaster.virtualdream.virtualdream.dtos.ServiceSaleDto;
import com.tinomaster.virtualdream.virtualdream.dtos.requests.ServiceSaleByBusinessAndDateDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualdream.entities.ServiceSale;
import com.tinomaster.virtualdream.virtualdream.services.ServiceSaleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ServiceSaleController {

	private final ServiceSaleService serviceSaleService;
	private final ModelMapper mapper;

	private ServiceSaleDto toDto(ServiceSale serviceSale) {
		return mapper.map(serviceSale, ServiceSaleDto.class);
	}

	@PostMapping("/private/serviceSale/getByBusinessAndDate")
	public ResponseEntity<ResponseBody<List<ServiceSaleDto>>> getServiceSalesByBusinessAndDate(
			@RequestBody ServiceSaleByBusinessAndDateDto requestDto) {
		System.out.println(requestDto);
		var serviceSaleList = StreamSupport
				.stream(serviceSaleService.getServiceSalesByBusinessIdAndDate(requestDto.getBusinessId(),
						requestDto.getStartDate(), requestDto.getEndDate()).spliterator(), false)
				.toList();

		List<ServiceSaleDto> serviceSales = serviceSaleList.stream().map(this::toDto).collect(Collectors.toList());
		return ResponseType.ok("successfullyRequest", serviceSales);
	}

	@PostMapping("/private/serviceSale")
	public ResponseEntity<ResponseBody<ServiceSaleDto>> saveServiceSale(@RequestBody ServiceSaleDto serviceSaleDto) {
		ServiceSaleDto serviceSaleSaved = this.toDto(serviceSaleService.saveServiceSale(serviceSaleDto));
		System.out.println(serviceSaleSaved);
		return ResponseType.ok("successfullySaved", serviceSaleSaved);
	}

	@DeleteMapping("/private/serviceSale/{id}")
	public ResponseEntity<ResponseBody<Object>> deleteServiceSale(@PathVariable Long id) {
		serviceSaleService.deleteServiceSale(id);
		return ResponseType.ok("successfullyDeleted");
	}

	@PutMapping("/private/serviceSale")
	public ResponseEntity<ResponseBody<ServiceSaleDto>> updateServiceSale(@RequestBody ServiceSaleDto serviceSaleDto) {
		ServiceSaleDto serviceSaleSaved = this
				.toDto(serviceSaleService.updateServiceSale(serviceSaleDto));
		return ResponseType.ok("successfullyUpdated", serviceSaleSaved);
	}

}
