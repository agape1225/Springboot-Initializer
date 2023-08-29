package com.springboot.initialize_project.service.impl;

import com.springboot.initialize_project.service.MaskingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MaskingServiceImpl implements MaskingService {

    private final Logger LOGGER = LoggerFactory.getLogger(MaskingServiceImpl.class);

    public String phoneMasking(String phoneNo) {

        LOGGER.info("[phoneMasking] 핸드폰 번호 마스킹 시작. 번호 : {}", phoneNo);

        String maskedNo = phoneNo;

        String regex = "(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$";

        Matcher matcher = Pattern.compile(regex).matcher(phoneNo);
        if(matcher.find()) {
            String target = matcher.group(2);
            int length = target.length();
            char[] c = new char[length];
            Arrays.fill(c, '*');

            maskedNo = phoneNo.replace(target, String.valueOf(c));
        }

        LOGGER.info("[phoneMasking] 핸드폰 번호 마스킹 완료. 번호 : {}", maskedNo);

        return maskedNo;
    }

    @Override
    public String crnMasking(String crn) {

        LOGGER.info("[phoneMasking] 사업자 번호 마스킹 시작. 번호 : {}", crn);

        String regex = "(\\d{3})-?(\\d{2})-?(\\d{5})$";
        String maskedCrn = crn;

        Matcher matcher = Pattern.compile(regex).matcher(crn);
        if(matcher.find()) {
            String target1 = matcher.group(2);
            int length1 = target1.length();
            char[] c1 = new char[length1];
            Arrays.fill(c1, '*');
            String crnTemp = crn.replace(target1, String.valueOf(c1));

            String target2 = matcher.group(3);
            int length2 = target2.length();
            char[] c2 = new char[length2];
            Arrays.fill(c2, '*');

            maskedCrn = crnTemp.replace(target2, String.valueOf(c2));
        }

        LOGGER.info("[phoneMasking] 핸드폰 번호 마스킹 완료. 번호 : {}", maskedCrn);

        return crn;
    }
}
