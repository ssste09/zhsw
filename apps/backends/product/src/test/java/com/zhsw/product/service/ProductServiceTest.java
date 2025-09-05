package com.zhsw.product.service;

import com.zhsw.product.entity.Product;
import com.zhsw.product.mapper.ProductMapper;
import com.zhsw.product.repository.ProductRepository;
import com.zhsw.product.utils.Category;
import com.zhsw.product.utils.Gender;
import com.zhsw.product.utils.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Tag("integration")
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnProductById() {

        Long productId = 1L;

        var productEntity = Product.builder()
                .productId(productId)
                .name("Air Jordan 1")
                .brand("Nike")
                .price(BigDecimal.valueOf(150))
                .stockQuantity(10)
                .gender(Gender.MEN)
                .size(Size.EU_42)
                .colour("Red")
                .category(Category.SNEAKERS)
                .images(List.of("https://example.com/images/jordan1.png"))
                .build();

        org.openapitools.model.Product mappedProduct = new org.openapitools.model.Product()
                .id(productId)
                .name("Air Jordan 1")
                .brand("Nike")
                .price(BigDecimal.valueOf(150));

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(productMapper.mapToProductResponse(productEntity)).thenReturn(mappedProduct);

        var result = productService.getProductById(productId);
    }

    @Test
    void shouldThrowGetProductById() {
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        var result = productService.getProductById(2L);

        assertTrue(result.isFailure());
        assertTrue(result.getCause() instanceof IllegalArgumentException);
        assertEquals("Product doesn't exist", result.getCause().getMessage());
    }

    @Test
    void shouldReturnAllProducts() {

        var product1 = Product.builder()
                .productId(1L)
                .name("Air Jordan 1")
                .price(BigDecimal.valueOf(150))
                .build();

        var product2 = Product.builder()
                .productId(2L)
                .name("Yeezy Boost")
                .price(BigDecimal.valueOf(200))
                .build();

        List<Product> productEntities = List.of(product1, product2);

        var mappedProduct1 =
                new org.openapitools.model.Product().id(1L).name("Air Jordan 1").price(BigDecimal.valueOf(150.0));

        var mappedProduct2 =
                new org.openapitools.model.Product().id(2L).name("Yeezy Boost").price(BigDecimal.valueOf(200.0));

        when(productRepository.findAll()).thenReturn(productEntities);
        when(productMapper.mapToProductResponse(product1)).thenReturn(mappedProduct1);
        when(productMapper.mapToProductResponse(product2)).thenReturn(mappedProduct2);

        var result = productService.getAllProducts();

        assertTrue(result.isSuccess());
        assertEquals(2, result.get().size());
        assertEquals("Air Jordan 1", result.get().get(0).getName());
        assertEquals("Yeezy Boost", result.get().get(1).getName());
    }
}
