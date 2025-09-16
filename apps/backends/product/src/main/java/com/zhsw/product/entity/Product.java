package com.zhsw.product.entity;

import com.zhsw.product.utils.Category;
import com.zhsw.product.utils.Gender;
import com.zhsw.product.utils.Size;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images;

    @NotNull(message = "Size is required")
    @Column(name = "size", nullable = false)
    @Enumerated(EnumType.STRING)
    private Size size;

    @Column(name = "gender", length = 16)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "colour")
    private String colour;

    @NotNull(message = "Category is required")
    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "discounted_price")
    private BigDecimal discountedPrice;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;
}