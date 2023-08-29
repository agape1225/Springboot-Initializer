package com.springboot.initialize_project.config.security;

import com.springboot.initialize_project.data.entity.JwtToken;
import com.springboot.initialize_project.data.entity.RefreshToken;
import com.springboot.initialize_project.data.repository.JwtTokenRepository;
import com.springboot.initialize_project.data.repository.RefreshTokenRepository;
import com.springboot.initialize_project.service.UserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final UserDetailsService userDetailsService;
    @Value("${springboot.jwt.secret}")
    private String secretKey = "secretKey";
    private final long tokenValidMillisecond = 1000L * 60 * 60;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenRepository jwtTokenRepository;

    @PostConstruct
    protected void init(){

        LOGGER.info("[init] JwtTokenProvider 내 secretKey 초기화를 진행합니다. secretKey : {}", secretKey);

        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));

        LOGGER.info("[init] JwtTokenProvider 내 secretKey 초기화를 완료하였습니다. secretKey : {}", secretKey);
    }

    public String createToken(String account){
        LOGGER.info("[createToken] 토큰 생성을 진행합니다. account : {}", account);

        Claims claims = Jwts.claims().setSubject(account);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        LOGGER.info("[createToken] 토큰 생성이 완료되었습니다. account : {}", account);

        return token;
    }

    public Authentication getAuthentication(String token){

        LOGGER.info("[getAuthentication] 토큰 인증 정보 조회를 진행합니다.");

        UserDetails userDetails = userDetailsService.loadUserByAccount(this.getAccount(token));

        LOGGER.info("[getAuthentication] 토큰 인증 정보 조회가 완료되었습니다. userDetails UserName : {}",
                userDetails.getAuthorities());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getAccount(String token){

        LOGGER.info("[getAccount] 토큰 기반 회원 구별 정보 추출을 진행합니다.");

        String account = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();

        LOGGER.info("[getAccount] 토큰 기반 회원 구별 정보 추출 완료, account : {}", account);

        return account;
    }

    public String resolveToken(HttpServletRequest request){

        LOGGER.info("[resolveToken] HTTP 헤더에서 Token 값 추출을 진행합니다.");

        String token = request.getHeader("AUTHORIZATION");

        LOGGER.info("[resolveToken] HTTP 헤더에서 Token 값 추출을 완료하였습니다.");

        return request.getHeader("AUTHORIZATION");
    }

    public boolean validateToken(String token){
        LOGGER.info("[validateToken] 토큰 유효 체크를 진행합니다.");
        try{
            Jws<Claims> claims = (Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token));

            return !claims.getBody().getExpiration().before(new Date());
        }catch (Exception e) {
            LOGGER.info("[validateToken] 토큰 유효 체크 예외가 발생하였습니다.");
            return false;
        }
    }

    public String generateRefreshToken() {

        LOGGER.info("[generateRefreshToken] refresh token 발급을 진행합니다.");

        String refreshToken = UUID.randomUUID().toString();

        LOGGER.info("[generateRefreshToken] refresh token 발급이 완료되었습니다.");

        return refreshToken;
    }

    public void addRefreshTokenToCookie(
            HttpServletRequest request,
            HttpServletResponse response,
            String refreshToken) {

        LOGGER.info("[addRefreshTokenToCookie] refresh token 추가를 진행합니다.");

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());
        cookie.setMaxAge((int) (tokenValidMillisecond / 1000));
        cookie.setPath("/");

        response.addCookie(cookie);

        LOGGER.info("[addRefreshTokenToCookie] refresh token 추가가 완료되었습니다.");
    }

    public RefreshToken getRefreshToken(HttpServletRequest request, HttpServletResponse response){

        LOGGER.info("[getRefreshToken] refresh token 조회를 진행합니다.");

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {

                    String refreshToken = cookie.getValue();
                    RefreshToken savedRefreshToken =  refreshTokenRepository.findRefreshTokenByToken(refreshToken);

                    LOGGER.info("[getRefreshToken] refresh token 조회가 완료되었습니다.");

                    return savedRefreshToken;
                }
            }
        }else
            LOGGER.info("[getRefreshToken] refresh token이 쿠키에 존재하지 않습니다.");

        return null;
    }

    public String validRefreshToken(HttpServletRequest request, HttpServletResponse response) {

        LOGGER.info("[validRefreshToken] refresh token 유효 체크를 진행합니다.");

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {

                    String refreshToken = cookie.getValue();
                    RefreshToken savedRefreshToken =  refreshTokenRepository.findRefreshTokenByToken(refreshToken);

                    return savedRefreshToken.getAccount();
                }
            }
        }else
            LOGGER.info("[validRefreshToken] refresh token이 쿠키에 존재하지 않습니다.");

        return null;
    }

    public boolean checkJwtTokenInDatabase(String token){

        LOGGER.info("[checkJwtTokenInDatabase] token 조회를 진행합니다.");

        JwtToken jwtToken = jwtTokenRepository.findJwtTokenByToken(token);

        return jwtToken.getAccount().equals(getAccount(token));
    }
}
