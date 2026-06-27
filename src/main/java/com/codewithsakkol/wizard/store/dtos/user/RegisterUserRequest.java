package com.codewithsakkol.wizard.store.dtos.user;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String username;
    private String email;
    private String password;
}
