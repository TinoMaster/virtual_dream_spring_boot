package com.tinomaster.virtualdream.virtualdream.dtos.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Genera getters, setters, toString, etc.
@NoArgsConstructor // Constructor sin argumentos
public class MonthYearRequestDto {
    private int year;
    private int month;
}
