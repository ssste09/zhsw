package com.zhsw.product.repository;

import com.zhsw.product.entity.Product;
import com.zhsw.product.utils.Category;
import com.zhsw.product.utils.Gender;
import com.zhsw.product.utils.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(
            """
        SELECT p
        FROM Product p
        WHERE p.name = :name
          AND p.brand = :brand
          AND p.description = :description
          AND p.size = :size
          AND p.gender = :gender
          AND p.colour = :colour
          AND p.category = :category
    """)
    Optional<Product> findExistingProduct(
            @Param("name") String name,
            @Param("brand") String brand,
            @Param("description") String description,
            @Param("size") Size size,
            @Param("gender") Gender gender,
            @Param("colour") String colour,
            @Param("category") Category category);
}
