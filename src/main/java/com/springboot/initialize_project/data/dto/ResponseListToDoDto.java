package com.springboot.initialize_project.data.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseListToDoDto {
    private List<ResponseToDoDto> items;

}
