package com.zhsw.product.service;

import com.zhsw.product.entity.Product;
import com.zhsw.product.mapper.ProductMapper;
import com.zhsw.product.repository.ProductRepository;
import com.zhsw.product.utils.Category;
import com.zhsw.product.utils.Gender;
import com.zhsw.product.utils.Size;
import io.vavr.control.Try;
import jakarta.transaction.Transactional;
import org.openapitools.model.AddProductRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public Try<Product> getProductById(Long id) {
        return Try.of(() -> productRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product doesn't exist")));
    }

    public Try<List<Product>> getAllProducts() {
        return Try.of(productRepository::findAll);
    }

    public Optional<Product> getExistingProduct(AddProductRequest req) {
        return productRepository.findExistingProduct(
                req.getName(),
                req.getBrand(),
                req.getDescription(),
                Size.fromOpenApi(req.getSize().name()),
                Gender.valueOf(req.getGender().name()),
                req.getColour(),
                Category.valueOf(req.getCategory().name()));
    }

    @Transactional
    public Try<Product> addProduct(AddProductRequest addProductRequest) {
        return Try.of(() -> getExistingProduct(addProductRequest)
                        .map((existing) -> updateStock(existing, addProductRequest.getStockQuantity()))
                        .orElseGet(() -> productMapper.mapProductRequestToProduct(addProductRequest)))
                .map(productRepository::save);
    }

    private Product updateStock(Product existing, int additionalQuantity) {
        existing.setStockQuantity(existing.getStockQuantity() + additionalQuantity);
        return existing;
    }
}
