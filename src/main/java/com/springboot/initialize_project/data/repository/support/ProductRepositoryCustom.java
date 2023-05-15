package com.springboot.initialize_project.data.repository.support;

import com.springboot.initialize_project.data.entity.Product;

import java.util.List;

public interface ProductRepositoryCustom {

    List<Product> findByName(String name);

}