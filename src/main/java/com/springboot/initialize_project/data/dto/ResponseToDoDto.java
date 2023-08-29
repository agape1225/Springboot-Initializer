package com.springboot.initialize_project.data.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseToDoDto {
    private Long id;
    private String name;
    private Boolean state;
    private LocalDateTime createdAt;

}
