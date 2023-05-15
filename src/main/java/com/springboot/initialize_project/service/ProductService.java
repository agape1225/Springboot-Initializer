package com.springboot.initialize_project.service;


import com.springboot.initialize_project.data.dto.ProductDto;
import com.springboot.initialize_project.data.dto.ProductResponseDto;

public interface ProductService {

    ProductResponseDto getProduct(Long number);

    ProductResponseDto saveProduct(ProductDto productDto);

    ProductResponseDto changeProductName(Long number, String name) throws Exception;

    void deleteProduct(Long number) throws Exception;

}