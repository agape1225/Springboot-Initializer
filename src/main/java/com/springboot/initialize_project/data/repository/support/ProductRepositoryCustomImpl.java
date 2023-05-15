package com.springboot.initialize_project.data.repository.support;

import java.util.List;

import com.springboot.initialize_project.data.entity.Product;
import com.springboot.initialize_project.data.entity.QProduct;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

// 예제 8.37
@Component
public class ProductRepositoryCustomImpl extends QuerydslRepositorySupport implements
    ProductRepositoryCustom {

    public ProductRepositoryCustomImpl() {
        super(Product.class);
    }

    @Override
    public List<Product> findByName(String name) {
        QProduct product = QProduct.product;

        List<Product> productList = from(product)
            .where(product.name.eq(name))
            .select(product)
            .fetch();

        return productList;
    }
}