package com.springboot.initialize_project.data.repository;

import com.springboot.initialize_project.data.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findRefreshTokenByToken(String token);
}
