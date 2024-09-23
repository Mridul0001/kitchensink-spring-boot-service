package com.kitchensink.ks.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends Response {
    private String token;
    private long expiresIn;
}
