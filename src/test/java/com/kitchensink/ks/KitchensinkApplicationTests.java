package com.kitchensink.ks;

import com.kitchensink.ks.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class KitchensinkApplicationTests {

    @Test
    void contextLoads() {
        // This test ensures that the Spring ApplicationContext is loaded successfully
    }
}
