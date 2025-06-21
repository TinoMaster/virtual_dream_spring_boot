package com.tinomaster.virtualdream.virtualdream.dtos;

import com.tinomaster.virtualdream.virtualdream.enums.ENoteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private ENoteStatus status;
    private LocalDateTime dateLimit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long businessId;
    private Long assignedToId;
}
