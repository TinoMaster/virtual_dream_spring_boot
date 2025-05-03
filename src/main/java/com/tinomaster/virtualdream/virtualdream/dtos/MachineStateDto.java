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
public class MachineStateDto {
    private Long id;
    private Long BusinessFinalSaleId;
    private MachineDto machine;
    private Float fund;
    private LocalDateTime date;
}
