package com.zhsw.product.mapper;

import com.zhsw.product.utils.Category;
import com.zhsw.product.utils.Gender;
import com.zhsw.product.utils.Size;
import lombok.Data;
import org.openapitools.model.AddProductRequest;
import org.openapitools.model.Product;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Data
public class ProductMapper {

    public Product mapToProductResponse(com.zhsw.product.entity.Product product) {
        return new Product()
                .id(product.getProductId())
                .name(product.getName())
                .colour(product.getColour())
                .description(product.getDescription())
                .brand(product.getBrand())
                .gender(mapToResponseGenderEnum(product.getGender()))
                .stockQuantity(product.getStockQuantity())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .discountedPrice(product.getDiscountedPrice())
                .size(mapToResponseSizeEnum(product.getSize()))
                .category(mapToResponseCategoryEnum(product.getCategory()))
                .imageUrl(
                        product.getImages() != null
                                ? product.getImages().stream()
                                        .map(URI::create) // Convert String → URI
                                        .collect(Collectors.toList())
                                : List.of());
    }

    public List<Product> mapToProductResponse(List<com.zhsw.product.entity.Product> products) {
        return products.stream().map(this::mapToProductResponse).toList();
    }

    public Product.GenderEnum mapToResponseGenderEnum(Gender gender) {
        if (gender == null) return null;
        return switch (gender.name().toUpperCase()) {
            case "WOMAN", "WOMEN" -> Product.GenderEnum.WOMEN;
            case "MEN" -> Product.GenderEnum.MEN;
            case "KIDS" -> Product.GenderEnum.KIDS;
            default -> throw new IllegalArgumentException("Unknown gender: " + gender);
        };
    }

    public Product.SizeEnum mapToResponseSizeEnum(Size size) {

        return Product.SizeEnum.valueOf(size.name());
    }

    public Product.CategoryEnum mapToResponseCategoryEnum(Category category) {

        return Product.CategoryEnum.valueOf(category.name());
    }

    public com.zhsw.product.entity.Product mapProductRequestToProduct(AddProductRequest request) {

        return com.zhsw.product.entity.Product.builder()
                .brand(request.getBrand())
                .colour(request.getColour())
                .discountedPrice(request.getDiscountedPrice())
                .price(request.getPrice())
                .name(request.getName())
                .category(Category.valueOf(request.getCategory().name()))
                .description(request.getDescription())
                .stockQuantity(request.getStockQuantity())
                .images(
                        request.getImageUrl() != null
                                ? request.getImageUrl().stream()
                                        .map(URI::toString)
                                        .toList()
                                : List.of())
                .size(Size.valueOf(request.getSize().name()))
                .gender(Gender.valueOf(request.getGender().name()))
                .discount(request.getDiscount())
                .build();
    }
}
