package com.springboot.initialize_project.data.repository;

import com.springboot.initialize_project.data.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
}
