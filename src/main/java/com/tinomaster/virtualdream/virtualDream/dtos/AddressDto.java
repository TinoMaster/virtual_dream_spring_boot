package com.tinomaster.virtualdream.virtualDream.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
	private Long id;
	private String street;
	private String number;
	private String city;
	private String zip;
}
