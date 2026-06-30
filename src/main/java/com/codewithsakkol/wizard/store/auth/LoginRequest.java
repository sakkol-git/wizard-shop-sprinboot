package com.codewithsakkol.wizard.store.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    @Email(message = "Input must be an email")
    private String email;
    @NotBlank
    private String password;
}
