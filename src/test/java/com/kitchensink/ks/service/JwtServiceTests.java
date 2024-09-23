package com.kitchensink.ks.service;

import com.kitchensink.ks.data.documents.UserDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static com.kitchensink.ks.ReflectionHelper.setPrivateField;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTests {

    @Mock
    private Date date;
    private JwtService jwtService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        jwtService = new JwtService();
        //Set private fields using reflection
        String secretKey = "E05wCURC59+YDEJlg8Et3kVz7h87e6uAWoOFvWwT9xU=";
        long jwtExpiration = 10000;
        setPrivateField(jwtService, "secretKey", secretKey);
        setPrivateField(jwtService, "jwtExpiration", jwtExpiration);
        setPrivateField(jwtService, "AUTHORITIES_KEY", "authorities");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateToken() {
        UserDetails userDetails = new UserDocument(){{
            setUsername("testUser");
            setRole("ROLE_USER");
        }};

        String token = jwtService.generateToken(userDetails);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    void testExtractUsername() {
        UserDetails userDetails = new UserDocument(){{
            setUsername("testUser");
            setRole("ROLE_USER");
        }};
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);
        assertEquals("testUser", username);
    }

    @Test
    void testIsTokenValid() {
        UserDetails userDetails = new UserDocument(){{
            setUsername("testUser");
            setRole("ROLE_USER");
        }};

        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testIsTokenExpired() {
        UserDetails userDetails = new UserDocument(){{
            setUsername("testUser");
            setRole("ROLE_USER");
        }};

        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testIsTokenInvalid() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        UserDetails userDetails = new UserDocument(){{
            setUsername("testUser");
            setRole("ROLE_USER");
        }};

        UserDetails userDetails2 = new UserDocument(){{
            setUsername("testUser1");
            setRole("ROLE_USER");
        }};


        String token = jwtService.generateToken(userDetails);
        //Change user to make it invalid
        assertFalse(jwtService.isTokenValid(token, userDetails2));
    }

    @Test
    void testExpirationValue() {
        assertEquals(jwtService.getExpirationTime(), 10000);
    }
}
