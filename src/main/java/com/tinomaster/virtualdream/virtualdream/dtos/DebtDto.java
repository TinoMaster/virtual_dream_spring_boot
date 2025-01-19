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
public class DebtDto {
    private Long id;
    private String name;
    private String description;
    private Float total;
    private Float paid;
    private Long businessFinalSale;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
