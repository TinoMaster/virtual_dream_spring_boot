package com.tinomaster.virtualdream.virtualdream.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardDto {
    private Long id;
    private String number;
    private Long businessFinalSale;
    private Float amount;
}
