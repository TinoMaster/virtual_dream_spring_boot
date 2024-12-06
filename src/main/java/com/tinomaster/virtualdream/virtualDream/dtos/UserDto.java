package com.tinomaster.virtualdream.virtualDream.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private long id;
    private String name;
    private String email;
    private String role;
    private boolean active;
    private long businessId;
}
