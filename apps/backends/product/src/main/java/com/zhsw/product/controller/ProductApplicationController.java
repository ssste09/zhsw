package com.zhsw.product.controller;

import org.openapitools.api.ProductsApi;
import org.openapitools.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductApplicationController implements ProductsApi {

    @Override
    public ResponseEntity<Product> getProductById(Integer productId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ResponseEntity<List<Product>> getAllProducts() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
