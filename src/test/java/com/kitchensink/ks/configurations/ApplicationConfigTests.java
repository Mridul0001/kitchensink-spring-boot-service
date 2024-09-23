package com.kitchensink.ks.configurations;

import com.kitchensink.ks.data.documents.UserDocument;
import com.kitchensink.ks.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ApplicationConfigTests {

    @Mock
    private UserRepository userRepository;

    private ApplicationConfig applicationConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        applicationConfig = new ApplicationConfig(userRepository);
    }

    @Test
    public void testUserDetailsService_UserExists() {
        // Arrange
        UserDocument mockUser = new UserDocument();
        mockUser.setUsername("testUser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));

        // Act
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();
        var userDetails = userDetailsService.loadUserByUsername("testUser");

        // Assert
        assertTrue(userDetails.getUsername().equals("testUser"));
    }

    @Test
    public void testUserDetailsService_UserNotFound() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();

        // Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("unknownUser");
        });
    }
}

