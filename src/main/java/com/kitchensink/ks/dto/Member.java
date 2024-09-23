package com.kitchensink.ks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member extends Response{
    private String id;

    @NotBlank(message = "Name is missing")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is missing")
    private String email;

    @Size(min = 10, max = 12, message = "Phone number must be at least 10 and at most 12 characters")
    private String phoneNumber;
}
