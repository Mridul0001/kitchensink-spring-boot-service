package com.kitchensink.ks.controller;

import com.kitchensink.ks.data.documents.UserDocument;
import com.kitchensink.ks.dto.LoginRequest;
import com.kitchensink.ks.dto.LoginResponse;
import com.kitchensink.ks.service.AuthenticationService;
import com.kitchensink.ks.service.JwtService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController for handling all user related operations like login, signup etc.
 * Base path is defined in application.properties file as /kitchensink/rest
 */
@RestController
@RequestMapping("/users")
@Log4j2
public class UserController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @Autowired
    public UserController(final AuthenticationService authenticationService,
                          final JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }


    /**
     * Authenticate a user by username and password
     * @param loginRequest Login request object containing username and password
     * @return LoginResponse with JWT token and expiration in milliseconds. May contain status code and errorMessage in case of exceptions
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest loginRequest) {
        UserDocument authenticatedUser = authenticationService.authenticate(loginRequest);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(){{
            setToken(jwtToken);
            setExpiresIn(jwtService.getExpirationTime());
        }};
        return ResponseEntity.ok(loginResponse);
    }
}
