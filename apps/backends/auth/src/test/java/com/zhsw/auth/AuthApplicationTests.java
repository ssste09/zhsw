package com.zhsw.auth;

import static org.junit.jupiter.api.Assertions.*;

import com.zhsw.auth.config.MockRepositoriesConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Tag("unit")
@EnableAutoConfiguration(exclude = {
		DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class
})
@Import(MockRepositoriesConfig.class)
class AuthApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void shouldFail() {
		var ciao = "ciao";
		assertEquals("ciao", ciao);
	}

}
