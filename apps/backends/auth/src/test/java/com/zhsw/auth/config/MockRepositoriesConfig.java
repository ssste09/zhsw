package com.zhsw.auth.config;
import com.zhsw.auth.repository.AddressRepository;
import com.zhsw.auth.repository.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.context.ApplicationContext;
import org.mockito.Mockito;

import java.util.Map;

@TestConfiguration
public class MockRepositoriesConfig {

    public MockRepositoriesConfig(ApplicationContext context) {
        // Automatically create mocks for all beans that extend CrudRepository
        Map<String, JpaRepository> repos = context.getBeansOfType(JpaRepository.class);
        repos.forEach((name, repo) -> {
            Mockito.reset(repo); // reset any previous state
        });
    }

    // Example: define a primary mock bean for a specific repository
    @Bean
    @Primary
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    @Primary
    public AddressRepository addressRepository() {
        return Mockito.mock(AddressRepository.class);
    }
}
