package com.tinomaster.virtualdream.virtualdream.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceKeyDto {
    private Long id;
    private LocalDateTime createdAt;
}

