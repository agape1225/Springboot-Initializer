package com.springboot.initialize_project.data.repository;

import com.springboot.initialize_project.data.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
