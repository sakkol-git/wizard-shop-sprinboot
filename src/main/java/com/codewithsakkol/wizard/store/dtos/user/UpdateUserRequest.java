package com.codewithsakkol.wizard.store.dtos.user;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;
    private String email;
}
