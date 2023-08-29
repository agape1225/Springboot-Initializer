package com.springboot.initialize_project.data.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String token;

    @Column(nullable = false, unique = true)
    private String account;
}
