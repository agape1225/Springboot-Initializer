package com.springboot.initialize_project.data.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    String nickname;
    String phone;
    String crn;
    LocalDateTime createdAt;
}
