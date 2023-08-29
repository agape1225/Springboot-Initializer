package com.springboot.initialize_project.data.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SignUpDto {
    String account;
    String password;
    String nickname;
    String phone;
    String crn;
}
