package com.springboot.initialize_project.service;

import com.springboot.initialize_project.data.dto.VerifyResponseDto;

public interface CrnService {
    VerifyResponseDto checkCrn(String crn);
}
