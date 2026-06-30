package com.codewithsakkol.wizard.store.users;

import com.codewithsakkol.wizard.store.users.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserRespond {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
