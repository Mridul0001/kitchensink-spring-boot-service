package com.kitchensink.ks.service;

import com.kitchensink.ks.data.documents.UserDocument;
import com.kitchensink.ks.data.repository.UserRepository;
import com.kitchensink.ks.dto.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTests {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticate_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        UserDocument userDocument = new UserDocument(){{
            setUsername("testUser");
            setPassword("testPassword");
        }};

        // Mock the authentication process
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("testUser", "testPassword"));

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userDocument));

        UserDocument result = authenticationService.authenticate(loginRequest);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testAuthenticate_UserNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        // Mock the authentication process
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("testUser", "testPassword"));

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authenticationService.authenticate(loginRequest));
    }
}
