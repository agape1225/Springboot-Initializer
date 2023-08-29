package com.springboot.initialize_project.data.repository;

import com.springboot.initialize_project.data.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    JwtToken findJwtTokenByToken(String token);
    JwtToken findJwtTokenByAccount(String account);
}
