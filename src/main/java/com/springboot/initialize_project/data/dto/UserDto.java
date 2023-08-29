package com.springboot.initialize_project.data.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String account;
    private String password;
    private String nickname;
    private String phone;
    private String crn;
    private Boolean status;
    private List<String> roles = new ArrayList<>();
}