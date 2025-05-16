package com.tinomaster.virtualdream.virtualdream.dtos;

import com.tinomaster.virtualdream.virtualdream.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuperAdminDto {
    private String name;
    private String email;
    private String password;
    private ERole role;
}
