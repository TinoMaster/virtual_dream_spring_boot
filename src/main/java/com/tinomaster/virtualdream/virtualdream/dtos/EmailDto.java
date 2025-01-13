package com.tinomaster.virtualdream.virtualdream.dtos;

import lombok.Data;

@Data
public class EmailDto {

	private String destination;
	private String subject;
	private String message;
}
