package com.springboot.initialize_project.data.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="jwt_token")
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String token;

    @Column(nullable = false, unique = true)
    private String account;
}
