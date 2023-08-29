package com.springboot.initialize_project.controller;

import com.springboot.initialize_project.config.security.JwtTokenProvider;
import com.springboot.initialize_project.data.dto.SignInDto;
import com.springboot.initialize_project.data.dto.SignInResultDto;
import com.springboot.initialize_project.service.SignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final SignService signService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(SignService signService, JwtTokenProvider jwtTokenProvider){
        this.signService = signService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping(value = "/login")
    public SignInResultDto signIn(@RequestBody SignInDto signInDto,
                                  HttpServletRequest servletRequest,
                                  HttpServletResponse servletResponse)
            throws RuntimeException {
        LOGGER.info("[login] 로그인을 시도하고 있습니다. account : {}, pw : ****", signInDto.getAccount());
        SignInResultDto signInResultDto =
                signService.signIn(
                        signInDto.getAccount(),
                        signInDto.getPassword(),
                        servletRequest,
                        servletResponse);

        if(signInDto == null)
            LOGGER.info("[login] 계정이 비활성화 상태입니다. account : {}", signInDto.getAccount());
        return signInResultDto;

    }

    @GetMapping("/logout")
    public void deleteToken(HttpServletRequest servletRequest,
                            HttpServletResponse servletResponse) {

        String token = jwtTokenProvider.resolveToken(servletRequest);
        String account = jwtTokenProvider.getAccount(token);

        LOGGER.info("[logout] 로그아웃을 시도하고 있습니다. account : {}", account);

        signService.deleteToken(servletRequest, servletResponse, token);

        LOGGER.info("[logout] 로그아웃이 완료되었습니다. account : {}", account);
    }

    @GetMapping(value = "/token")
    public SignInResultDto getRefreshToken(HttpServletRequest servletRequest,
                                           HttpServletResponse servletResponse) {

        String token = jwtTokenProvider.resolveToken(servletRequest);
        String account = jwtTokenProvider.getAccount(token);

        LOGGER.info("[getRefreshToken] refresh token 발급을 시도하고 있습니다. account : {}", account);

        SignInResultDto signInResultDto = signService.getNewToken(servletRequest, servletResponse);

        LOGGER.info("[getRefreshToken] refresh token 발급이 완료되었습니다. account : {}", account);
        return signInResultDto;
    }
}
