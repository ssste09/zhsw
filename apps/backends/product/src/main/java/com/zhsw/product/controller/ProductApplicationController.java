package com.zhsw.product.controller;

import com.zhsw.product.mapper.ProductMapper;
import com.zhsw.product.service.ProductService;
import org.openapitools.api.ProductsApi;
import org.openapitools.model.AddProductRequest;
import org.openapitools.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductApplicationController implements ProductsApi {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductApplicationController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Override
    public ResponseEntity<Product> getProductById(Long productId) {
        return productService
                .getProductById(productId)
                .map(productMapper::mapToProductResponse)
                .map(ResponseEntity::ok)
                .get();
    }

    @Override
    public ResponseEntity<List<Product>> getAllProducts() {
        return productService
                .getAllProducts()
                .map(productMapper::mapToProductResponse)
                .map(ResponseEntity::ok)
                .get();
    }

    @Override
    public ResponseEntity<Product> addProduct(AddProductRequest addProductRequest) {
        return productService
                .addProduct(addProductRequest)
                .map(productMapper::mapToProductResponse)
                .map(ResponseEntity::ok)
                .get();
    }
}
