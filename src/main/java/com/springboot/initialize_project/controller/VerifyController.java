package com.springboot.initialize_project.controller;

import com.springboot.initialize_project.data.dto.VerifyResponseDto;
import com.springboot.initialize_project.service.CrnService;
import com.springboot.initialize_project.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify")
public class VerifyController {
    private final Logger LOGGER = LoggerFactory.getLogger(VerifyController.class);
    private final UserService userService;
    private final CrnService crnService;

    @Autowired
    public VerifyController(UserService userService, CrnService crnService){
        this.userService = userService;
        this.crnService = crnService;
    }

    @GetMapping("/account")
    public ResponseEntity<VerifyResponseDto> checkAccount(@RequestParam(value = "account", required = true) String account) {

        LOGGER.info("[checkAccount] 계정 중복 조회를 시도하고 있습니다. account : {}", account);

        VerifyResponseDto verifyResponseDto = userService.checkAccountDuplicate(account);

        LOGGER.info("[checkAccount] 계정 정보 조회를 완료하였습니다. account : {}", account);

        return ResponseEntity.status(HttpStatus.OK).body(verifyResponseDto);

    }

    @GetMapping("/nickname")
    public ResponseEntity<VerifyResponseDto> checkNickname ( @RequestParam(value = "nickname", required = true) String nickname) {

        LOGGER.info("[checkNickname] 이름 중복 조회를 시도하고 있습니다. nickname : {}", nickname);

        VerifyResponseDto verifyResponseDto = userService.checkNicknameDuplicate(nickname);

        LOGGER.info("[checkNickname] 이름 정보 조회를 완료하였습니다. nickname : {}", nickname);

        return ResponseEntity.status(HttpStatus.OK).body(verifyResponseDto);

    }

    @GetMapping("/crn")
    public ResponseEntity<VerifyResponseDto> checkCrn ( @RequestParam(value = "crn", required = true) String crn) {

        LOGGER.info("[checkCrn] 사업자 번호 유효성 체크를 시도하고 있습니다. crn : {}", crn);

        VerifyResponseDto verifyResponseDto = crnService.checkCrn(crn);

        LOGGER.info("[checkCrn] 사업자 번호 유효성 체크를 조회를 완료하였습니다. crn : {}", crn);

        return ResponseEntity.status(HttpStatus.OK).body(verifyResponseDto);

    }
}
