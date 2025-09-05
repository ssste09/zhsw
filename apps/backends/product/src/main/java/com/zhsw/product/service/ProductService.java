package com.zhsw.product.service;

import com.zhsw.product.entity.Product;
import com.zhsw.product.repository.ProductRepository;
import io.vavr.control.Try;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Try<Product> getProductById(Long id) {
        return Try.of(() -> productRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product doesn't exist")));
    }

    public Try<List<Product>> getAllProducts() {
        return Try.of(productRepository::findAll);
    }
}
