package com.springboot.initialize_project.data.repository;

import com.springboot.initialize_project.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User getByAccount(String account);
    Optional<User> findByAccount(String account);
    Optional<User> findByNickname(String account);
}
