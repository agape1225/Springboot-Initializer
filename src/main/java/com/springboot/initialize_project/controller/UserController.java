package com.springboot.initialize_project.controller;

import com.springboot.initialize_project.config.security.JwtTokenProvider;
import com.springboot.initialize_project.data.dto.SignUpDto;
import com.springboot.initialize_project.data.dto.UserResponseDto;
import com.springboot.initialize_project.service.SignService;
import com.springboot.initialize_project.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final SignService signService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public UserController(SignService signService,
                          JwtTokenProvider jwtTokenProvider,
                          UserService userService){

        this.signService = signService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping()
    public void signUp(@RequestBody SignUpDto signUpDto){

        LOGGER.info("[signUp] 회원가입을 시도하고 있습니다. account : {}", signUpDto.getAccount());

        signService.signUp(
                signUpDto.getAccount(),
                signUpDto.getPassword(),
                signUpDto.getNickname(),
                signUpDto.getPhone(),
                signUpDto.getCrn()
        );

        LOGGER.info("[signUp] 회원가입을 완료하였습니다. account : {}", signUpDto.getAccount());
    }

    @GetMapping()
    public ResponseEntity<UserResponseDto> getUser(HttpServletRequest servletRequest) {

        String account =jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(servletRequest));

        LOGGER.info("[getUser] user 정보 조회를 시도하고 있습니다. account : {}", account);

        UserResponseDto userResponseDto = userService.getUserByAccount(account);

        LOGGER.info("[getUser] user 정보 조회를 완료하였습니다. account : {}", account);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @DeleteMapping
    public void deleteUser(HttpServletRequest servletRequest){

        String account =jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(servletRequest));

        LOGGER.info("[deleteUser] user 정보 삭제를 시도하고 있습니다. account : {}", account);

        userService.deleteUser(account);

        LOGGER.info("[deleteUser] user 정보 삭제를 완료하였습니다. account : {}", account);
    }
}
