package com.springboot.initialize_project.data.dto;

import lombok.Data;
@Data
public class ToDoDto {
    private Long id;
    private String name;
    private Boolean state;
    private String description;
}
