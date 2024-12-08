package com.tinomaster.virtualdream.virtualDream.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusinessDto {
	private long id;
	private String name;
	private String email;
	private String phone;
	private String description;
	private String address;
}
