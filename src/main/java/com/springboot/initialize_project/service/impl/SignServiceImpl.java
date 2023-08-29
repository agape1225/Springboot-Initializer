package com.springboot.initialize_project.service.impl;

import com.springboot.initialize_project.config.security.JwtTokenProvider;
import com.springboot.initialize_project.data.dto.SignInResultDto;
import com.springboot.initialize_project.data.entity.JwtToken;
import com.springboot.initialize_project.data.entity.RefreshToken;
import com.springboot.initialize_project.data.entity.User;
import com.springboot.initialize_project.data.repository.JwtTokenRepository;
import com.springboot.initialize_project.data.repository.RefreshTokenRepository;
import com.springboot.initialize_project.data.repository.UserRepository;
import com.springboot.initialize_project.service.SignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@Service
public class SignServiceImpl implements SignService {

    private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);

    public UserRepository userRepository;
    public JwtTokenProvider jwtTokenProvider;
    public PasswordEncoder passwordEncoder;
    public RefreshTokenRepository refreshTokenRepository;
    public JwtTokenRepository jwtTokenRepository;

    @Autowired
    public SignServiceImpl(
            UserRepository   userRepository,
            JwtTokenProvider jwtTokenProvider,
            PasswordEncoder  passwordEncoder,
            RefreshTokenRepository refreshTokenRepository,
            JwtTokenRepository jwtTokenRepository){

        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenRepository = jwtTokenRepository;
    };

    @Override
    public void signUp(
            String account,
            String password,
            String nickname,
            String phone,
            String crn) {

        LOGGER.info("[signUp] 회원가입정보를 전달하였습니다.");

        User user;

        user = User.builder()
                .account(account)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .phone(phone)
                .crn(crn)
                .roles(Collections.singletonList("USER"))
                .status(true)
                .build();

        User savedUser = userRepository.save(user);

        LOGGER.info("[signUp] userEntity 값이 들어왔는지 확인 후 결과값 주입 진행");

        if(!savedUser.getAccount().isEmpty())
            LOGGER.info("[signUp] 회원 정보가 정상적으로 처리되었습니다.");
        else
            LOGGER.info("[signUp] 회원 정보가 저장되지 않았습니다.");
    }

    @Override
    public SignInResultDto signIn(String account, String password, HttpServletRequest servletRequest,
                                  HttpServletResponse servletResponse) throws RuntimeException {

        LOGGER.info("[signIn] signIn 메소드로 로그인을 시도하였습니다. account : {}, password : {}", account, password);

        User user = userRepository.getByAccount(account);

        LOGGER.info("[signIn] user 데이터 추출이 완료되었습니다. user : {}", user);

        if(!user.getStatus()){
            LOGGER.info("[signIn] user가 비활성화 상태입니다.");
            return null;
        }

        LOGGER.info("[signIn] 패스워드 비교를 수행합니다.");
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException();
        }
        LOGGER.info("[signIn] 패스워드가 일치합니다.");

        LOGGER.info("[signIn] SignInResultDto 객체를 생성합니다.");
        String token = jwtTokenProvider.createToken(String.valueOf(user.getAccount()));
        SignInResultDto signInResultDto = SignInResultDto.builder()
                .token(token)
                .build();

        LOGGER.info("[signIn] cookie에 refresh token을 주입합니다.");
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        jwtTokenProvider.addRefreshTokenToCookie(servletRequest, servletResponse, refreshToken);

        LOGGER.info("[signIn] database에 refresh token을 주입합니다.");
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setToken(refreshToken);
        newRefreshToken.setAccount(account);
        refreshTokenRepository.save(newRefreshToken);

        LOGGER.info("[signIn] database에 token을 주입합니다.");
        JwtToken newJwtToken = new JwtToken();
        newJwtToken.setToken(token);
        newJwtToken.setAccount(account);
        jwtTokenRepository.save(newJwtToken);

        LOGGER.info("[signIn] SignInResultDto 객체에 값을 주입합니다.");

        return signInResultDto;
    }

    @Override
    public SignInResultDto getNewToken(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

        LOGGER.info("[getNewToken] 새로운 토큰을 발급합니다.");

        String account = jwtTokenProvider.validRefreshToken(servletRequest, servletResponse);
        SignInResultDto signInResultDto = new SignInResultDto();

        if(account != null){

            LOGGER.info("[getNewToken] 기존의 토큰을 찾아 제거합니다. account : {}", account);
            JwtToken savedJwtToken = jwtTokenRepository.findJwtTokenByAccount(account);
            jwtTokenRepository.delete(savedJwtToken);

            LOGGER.info("[getNewToken] 새로운 토큰을 생성하고 추가합니다. account : {}", account);
            String newToken = jwtTokenProvider.createToken(account);
            JwtToken newJwtToken = new JwtToken();
            newJwtToken.setAccount(account);
            newJwtToken.setToken(newToken);
            jwtTokenRepository.save(newJwtToken);

            LOGGER.info("[getNewToken] 생성된 토큰을 dto에 저장합니다. account : {}", account);
            signInResultDto.setToken(newToken);

        }else
            LOGGER.info("[getNewToken] 인증에 실패하였습니다.");

        return signInResultDto;
    }

    @Override
    public void deleteToken(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String token) {

        LOGGER.info("[deleteToken] 토큰 삭제를 진행합니다.");

        RefreshToken savedRefreshToken = jwtTokenProvider.getRefreshToken(servletRequest, servletResponse);
        JwtToken savedJwtToken = jwtTokenRepository.findJwtTokenByToken(token);
        jwtTokenRepository.delete(savedJwtToken);
        refreshTokenRepository.delete(savedRefreshToken);

        LOGGER.info("[deleteToken] 토큰 삭제가 완료되었습니다.");
    }
}
