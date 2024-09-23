package com.kitchensink.ks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {
    @NotBlank(message = "Username can not be empty")
    private String username;
    @NotBlank(message = "Password can not be empty")
    private String password;
}
