package com.esprit.userservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @Valid

    @Email(message = "inavalid mail format")
    @NotBlank(message = "email is required and cannot be blank.")
    private String username;
    @NotBlank(message = "password is required and cannot be blank.")
    private String password;
}
