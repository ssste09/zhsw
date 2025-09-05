package com.zhsw.product.config;

import com.zhsw.product.repository.ProductRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

@TestConfiguration
public class MockRepositoryConfig {

    public MockRepositoryConfig(ApplicationContext context) {
        // Automatically create mocks for all beans that extend CrudRepository
        Map<String, JpaRepository> repos = context.getBeansOfType(JpaRepository.class);
        repos.forEach((name, repo) -> {
            Mockito.reset(repo); // reset any previous state
        });
    }

    // Example: define a primary mock bean for a specific repository
    @Bean
    @Primary
    public ProductRepository userRepository() {
        return Mockito.mock(ProductRepository.class);
    }
}
