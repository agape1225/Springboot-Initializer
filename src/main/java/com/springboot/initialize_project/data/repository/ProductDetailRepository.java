package com.springboot.initialize_project.data.repository;

import com.springboot.initialize_project.data.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
}
