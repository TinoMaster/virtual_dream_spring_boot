package com.tinomaster.virtualdream.virtualDream.dtos;

import lombok.Data;

@Data
public class EmailDto {

	private String destination;
	private String subject;
	private String message;
}
