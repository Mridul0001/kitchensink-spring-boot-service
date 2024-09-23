package com.kitchensink.ks.controller;

import com.kitchensink.ks.data.documents.UserDocument;
import com.kitchensink.ks.dto.LoginRequest;
import com.kitchensink.ks.dto.LoginResponse;
import com.kitchensink.ks.service.AuthenticationService;
import com.kitchensink.ks.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class UserControllerTests {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticate() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest(){{
            setUsername("username");
            setPassword("password");
        }};
        UserDocument userDocument = new UserDocument();
        userDocument.setUsername("user");

        when(authenticationService.authenticate(loginRequest)).thenReturn(userDocument);
        when(jwtService.generateToken(userDocument)).thenReturn("jwt-token");
        when(jwtService.getExpirationTime()).thenReturn(3600000L);

        // Act
        ResponseEntity<LoginResponse> response = userController.authenticate(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token", response.getBody().getToken());
        assertEquals(3600000L, response.getBody().getExpiresIn());
        verify(authenticationService, times(1)).authenticate(loginRequest);
        verify(jwtService, times(1)).generateToken(userDocument);
        verify(jwtService, times(1)).getExpirationTime();
    }
}
