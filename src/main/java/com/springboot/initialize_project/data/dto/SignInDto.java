package com.springboot.initialize_project.data.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SignInDto {
    String account;
    String password;
}
