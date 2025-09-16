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
import org.openapitools.model.AddProductRequest;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
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

    @Test
    void shouldSaveAndReturnProduct() {
        var request = new AddProductRequest();
        request.setName("Sneaker");
        request.setBrand("Nike");
        request.setDescription("Running shoe");
        request.setSize(AddProductRequest.SizeEnum._40); // OpenAPI enum
        request.setGender(AddProductRequest.GenderEnum.MEN);
        request.setColour("Blue");
        request.setCategory(AddProductRequest.CategoryEnum.SNEAKERS);
        request.setPrice(BigDecimal.valueOf(100));
        request.setDiscount(BigDecimal.valueOf(10));
        request.setDiscountedPrice(BigDecimal.valueOf(90));
        request.setStockQuantity(5);
        request.setImageUrl(List.of(URI.create("https://example.com/images/sneaker.png")));

        // Mocking repository: no existing product
        when(productRepository.findExistingProduct(
                        anyString(),
                        anyString(),
                        anyString(),
                        any(Size.class),
                        any(Gender.class),
                        anyString(),
                        any(Category.class)))
                .thenReturn(Optional.empty());

        // Mocking mapper and save
        Product mappedProduct = Product.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .description(request.getDescription())
                .size(Size.fromOpenApi(request.getSize().name()))
                .gender(Gender.valueOf(request.getGender().name()))
                .colour(request.getColour())
                .category(Category.valueOf(request.getCategory().name()))
                .price(request.getPrice())
                .discount(request.getDiscount())
                .discountedPrice(request.getDiscountedPrice())
                .stockQuantity(request.getStockQuantity())
                .images(request.getImageUrl().stream().map(URI::toString).toList())
                .build();

        when(productMapper.mapProductRequestToProduct(any(AddProductRequest.class)))
                .thenReturn(mappedProduct);
        when(productRepository.save(any())).thenReturn(mappedProduct);

        var result = productService.addProduct(request);

        assertTrue(result.isSuccess());
        assertEquals("Sneaker", result.get().getName());
        assertEquals(Size.fromOpenApi("_40"), result.get().getSize());
    }

    private AddProductRequest createValidRequest() {
        AddProductRequest req = new AddProductRequest();
        req.setName("Sneaker");
        req.setBrand("Nike");
        req.setDescription("Running shoe");
        req.setSize(AddProductRequest.SizeEnum._40);
        req.setGender(AddProductRequest.GenderEnum.MEN);
        req.setColour("Blue");
        req.setCategory(AddProductRequest.CategoryEnum.SNEAKERS);
        req.setStockQuantity(5);
        return req;
    }

    @Test
    @WithMockUser(
            username = "admin@example.com",
            roles = {"ADMIN"})
    void shouldAllowAccessForAdminRole() {

        AddProductRequest request = createValidRequest();

        Product mappedProduct = Product.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .description(request.getDescription())
                .size(Size.fromOpenApi(request.getSize().name()))
                .gender(Gender.valueOf(request.getGender().name()))
                .colour(request.getColour())
                .category(Category.valueOf(request.getCategory().name()))
                .stockQuantity(request.getStockQuantity())
                .build();

        when(productMapper.mapProductRequestToProduct(request)).thenReturn(mappedProduct);
        when(productRepository.save(mappedProduct)).thenReturn(mappedProduct);

        var result = productService.addProduct(request);

        assertTrue(result.isSuccess());
        assertEquals("Sneaker", result.get().getName());
    }

    

    @Test
    void shouldReturnProductWhenProductExists() {
        AddProductRequest req = createValidRequest();

        Product existingProduct = Product.builder()
                .name(req.getName())
                .brand(req.getBrand())
                .description(req.getDescription())
                .size(Size.fromOpenApi(req.getSize().name()))
                .gender(Gender.valueOf(req.getGender().name()))
                .colour(req.getColour())
                .category(Category.valueOf(req.getCategory().name()))
                .stockQuantity(10)
                .build();

        when(productRepository.findExistingProduct(
                        eq("Sneaker"),
                        eq("Nike"),
                        eq("Running shoe"),
                        eq(Size.fromOpenApi(req.getSize().name())),
                        eq(Gender.valueOf(req.getGender().name())),
                        eq("Blue"),
                        eq(Category.valueOf(req.getCategory().name()))))
                .thenReturn(Optional.of(existingProduct));

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product result = productService.addProduct(req).get();

        assertNotNull(result);
        assertEquals(15, result.getStockQuantity());
        assertSame(existingProduct, result);
        verify(productRepository).save(existingProduct);
    }
}
