package com.zhsw.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Tag("unit")
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
