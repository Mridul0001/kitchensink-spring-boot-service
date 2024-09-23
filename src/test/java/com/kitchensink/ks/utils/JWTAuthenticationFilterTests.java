package com.kitchensink.ks.utils;

import com.kitchensink.ks.data.documents.UserDocument;
import com.kitchensink.ks.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JWTAuthenticationFilterTests {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HandlerExceptionResolver handlerExceptionResolver;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(null);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doFilterInternal_WithValidToken_ShouldSetAuthentication() throws ServletException, IOException {
        String token = "Bearer valid.token";
        String username = "testUser";

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtService.extractUsername("valid.token")).thenReturn(username);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid("valid.token", userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_WithInvalidToken_ShouldNotSetAuthentication() throws ServletException, IOException {
        String token = "Bearer invalid.token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtService.extractUsername("invalid.token")).thenThrow(new RuntimeException("Invalid Token"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(handlerExceptionResolver).resolveException(any(), any(), isNull(), any(Exception.class));
        // Verify no authentication was set
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_WithMissingToken_ShouldProceedWithoutAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithException_ShouldResolveException() throws ServletException, IOException {
        String token = "Bearer valid.token";
        String username = "testUser";

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtService.extractUsername("valid.token")).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenThrow(new RuntimeException("User not found"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(handlerExceptionResolver).resolveException(any(), any(), isNull(), any(Exception.class));
    }

    @Test
    void doFilterInternal_WithOutAuthHeader() throws ServletException, IOException {
        String token = "missing.bearer.token";
        when(request.getHeader("Authorization")).thenReturn(null);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        when(request.getHeader("Authorization")).thenReturn(token);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain,times(2)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_WithValidJWT() throws ServletException, IOException {
        // Mock the request, response, and filter chain
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // Mock JWT, username, and user details
        String jwt = "valid-jwt-token";
        String username = "testUser";
        UserDetails userDetails = new UserDocument(){{
            setUsername("testUser");
            setRole("ROLE_USER");
        }};

        // Set up the JWTService and UserDetailsService mocks
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(jwt, userDetails)).thenReturn(true);

        // Call the method under test
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(jwtService, userDetailsService, handlerExceptionResolver);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify that SecurityContext was set with a valid authentication token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);
        assertEquals(username, authentication.getName());

        // Verify the filter chain continues
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_WithBlankUsername() throws ServletException, IOException {
        // Mock the request, response, and filter chain
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // Mock JWT with blank username
        String jwt = "invalid-jwt-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn("");

        // Call the method under test
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(jwtService, userDetailsService, handlerExceptionResolver);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify no authentication was set in SecurityContext
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verify the filter chain continues
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_WithExistingAuthentication() throws ServletException, IOException {
        // Mock the request, response, and filter chain
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // Mock JWT, username, and user details
        String jwt = "valid-jwt-token";
        String username = "testUser";
        Authentication existingAuthentication = mock(Authentication.class);
        when(existingAuthentication.getName()).thenReturn(username);

        // Set up JWTService and existing authentication
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(username);
        SecurityContextHolder.getContext().setAuthentication(existingAuthentication);

        // Call the method under test
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(jwtService, userDetailsService, handlerExceptionResolver);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify that no new authentication was set
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).isTokenValid(anyString(), any(UserDetails.class));

        // Verify the filter chain continues
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_WithInvalidJWT() throws ServletException, IOException {
        // Mock the request, response, and filter chain
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // Mock JWT, username, and user details
        String jwt = "invalid-jwt-token";
        String username = "testUser";
        UserDetails userDetails = new UserDocument(){{
            setUsername("testUser");
            setRole("ROLE_USER");
        }};

        // Set up JWTService and UserDetailsService mocks
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(jwt, userDetails)).thenReturn(false);

        // Call the method under test
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(jwtService, userDetailsService, handlerExceptionResolver);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify no authentication was set in SecurityContext
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verify the filter chain continues
        verify(filterChain, times(1)).doFilter(request, response);
    }


}

