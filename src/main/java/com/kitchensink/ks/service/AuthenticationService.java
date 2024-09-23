package com.kitchensink.ks.service;

import com.kitchensink.ks.data.documents.UserDocument;
import com.kitchensink.ks.data.repository.UserRepository;
import com.kitchensink.ks.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public UserDocument authenticate(LoginRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        ).getDetails();

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
