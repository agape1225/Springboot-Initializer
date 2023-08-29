package com.springboot.initialize_project.service.impl;

import com.springboot.initialize_project.data.dto.VerifyResponseDto;
import com.springboot.initialize_project.service.CrnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CrnServiceImpl implements CrnService {

    private final Logger LOGGER = LoggerFactory.getLogger(CrnServiceImpl.class);
    @Override
    public VerifyResponseDto checkCrn(String crn) {

        LOGGER.info("[checkCrn] crn 번호 검증을 시작합니다. crn : {}", crn);

        VerifyResponseDto verifyResponseDto = new VerifyResponseDto();
        verifyResponseDto.setVerify(false);
        crn = crn.replaceAll("[^0-9]", "");

        if (crn.length() == 10) {
            int[] key = {1, 3, 7, 1, 3, 7, 1, 3, 5};

            int num1 = 0;
            int num2 = ((crn.charAt(8) - '0') * key[8]) / 10;

            for (int i = 0; i < 9; i++)
                num1 += (crn.charAt(i) - '0') * key[i];

            if(10 - ((num1 + num2) % 10) == 1)
                verifyResponseDto.setVerify(true);
        }

        LOGGER.info("[checkCrn] crn 번호 검증이 완료 되었습니다. 결과: {}", verifyResponseDto);

        return verifyResponseDto;
    }
}
