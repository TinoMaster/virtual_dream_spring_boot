package com.tinomaster.virtualdream.virtualDream.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tinomaster.virtualdream.virtualDream.dtos.BusinessFinalSaleDto;
import com.tinomaster.virtualdream.virtualDream.dtos.requests.BusinessFinalSaleByBusinessAndDateDto;
import com.tinomaster.virtualdream.virtualDream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualDream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualDream.entities.BusinessFinalSale;
import com.tinomaster.virtualdream.virtualDream.services.BusinessFinalSaleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class BusinessFinalSaleController {

	private final BusinessFinalSaleService businessFinalSaleService;
	private final ModelMapper mapper;

	private BusinessFinalSaleDto entityToDto(BusinessFinalSale businessFinalSale) {
		return mapper.map(businessFinalSale, BusinessFinalSaleDto.class);
	}

	@GetMapping("/private/business-final-sale")
	public ResponseEntity<ResponseBody<List<BusinessFinalSaleDto>>> getBusinessFinalSaleByBusinessAndDate(
			@RequestBody BusinessFinalSaleByBusinessAndDateDto requestDto) {
		var businessFinalSaleList = StreamSupport
				.stream(businessFinalSaleService.getBusinessFinalSaleByBusinessAndDate(requestDto.getBusinessId(),
						requestDto.getStartDate(), requestDto.getEndDate()).spliterator(), false)
				.toList();

		List<BusinessFinalSaleDto> businessFinalSaleDtoList = businessFinalSaleList.stream().map(this::entityToDto)
				.collect(Collectors.toList());
		return ResponseType.ok("successfullyRequest", businessFinalSaleDtoList);
	}

	@PostMapping("/private/business-final-sale")
	public ResponseEntity<ResponseBody<BusinessFinalSaleDto>> saveBusinessFinalSale(
			@RequestBody BusinessFinalSaleDto businessFinalSaleDto) {
		BusinessFinalSaleDto businessFinalSaleDtoToReturn = this
				.entityToDto(businessFinalSaleService.saveBusinessFinalSale(businessFinalSaleDto));
		return ResponseType.ok("successfullySaved", businessFinalSaleDtoToReturn);
	}
}
