package com.zhsw.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Brand is required")
    @Column(name = "brand", nullable = false)
    private String brand;

    @Lob
    @Column(name = "description")
    private String description;

    @NotBlank(message = "Images are required")
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images;

    @NotBlank(message = "Size is required")
    @Column(name = "size", nullable = false)
    @Enumerated(EnumType.STRING)
    private String size;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private String gender;

    @Column(name = "colour")
    private String colour;

    @NotBlank(message = "Category is required")
    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private String category;

    @NotBlank(message = "Price is required")
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "discounted_price")
    private BigDecimal discountedPrice;

    @NotBlank(message = "Stock quantity is required")
    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;
}